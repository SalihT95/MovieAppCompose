import os
import shutil
from PIL import Image, ImageDraw, ImageFont, ImageFilter

def create_rounded_mask(size, radius):
    mask = Image.new('L', size, 0)
    draw = ImageDraw.Draw(mask)
    draw.rounded_rectangle([(0, 0), size], radius=radius, fill=255)
    return mask

def make_gradient(width, height, top_rgb, bottom_rgb):
    base = Image.new('RGB', (width, height), top_rgb)
    draw = ImageDraw.Draw(base)
    for y in range(height):
        factor = y / float(height)
        r = int(top_rgb[0] + factor * (bottom_rgb[0] - top_rgb[0]))
        g = int(top_rgb[1] + factor * (bottom_rgb[1] - top_rgb[1]))
        b = int(top_rgb[2] + factor * (bottom_rgb[2] - top_rgb[2]))
        draw.line([(0, y), (width, y)], fill=(r, g, b))
    return base

def add_glow(base_img, cx, cy, radius, rgb_color, intensity=0.30):
    glow_size = radius * 2
    glow_img = Image.new('RGBA', (glow_size, glow_size), (0, 0, 0, 0))
    draw = ImageDraw.Draw(glow_img)
    for r in range(radius, 0, -4):
        alpha = int(255 * intensity * (1.0 - (r / float(radius)) ** 1.4))
        draw.ellipse([(radius - r, radius - r), (radius + r, radius + r)], fill=(rgb_color[0], rgb_color[1], rgb_color[2], alpha))
    glow_blurred = glow_img.filter(ImageFilter.GaussianBlur(40))
    base_img.paste(glow_blurred, (cx - radius, cy - radius), glow_blurred)

def render_store_screenshot(
    raw_screenshot_path,
    output_path,
    badge_text,
    title_text,
    subtitle_text,
    accent_color=(239, 68, 68),
    canvas_w=1080,
    canvas_h=1920
):
    # 1. Base Canvas & Background Gradient (Deep Cinema Blue-Black)
    canvas = make_gradient(canvas_w, canvas_h, (13, 17, 28), (3, 5, 10)).convert('RGBA')

    # Ambient Glow behind device
    add_glow(canvas, canvas_w // 2, 680, 420, accent_color, intensity=0.26)
    add_glow(canvas, canvas_w // 2, 1300, 380, (15, 23, 42), intensity=0.28)

    # 2. Fonts
    font_bold = r'C:\Windows\Fonts\segoeuib.ttf'
    font_regular = r'C:\Windows\Fonts\segoeui.ttf'
    font_badge = ImageFont.truetype(font_bold, 19)
    font_title = ImageFont.truetype(font_bold, 46)
    font_sub = ImageFont.truetype(font_regular, 22)

    draw = ImageDraw.Draw(canvas)

    # 3. Badge Pill
    badge_bbox = draw.textbbox((0, 0), badge_text, font=font_badge)
    badge_tw = badge_bbox[2] - badge_bbox[0]
    badge_th = badge_bbox[3] - badge_bbox[1]
    pill_w = badge_tw + 44
    pill_h = 38
    pill_x = (canvas_w - pill_w) // 2
    pill_y = 50

    # Pill background
    pill_bg = Image.new('RGBA', (pill_w, pill_h), (accent_color[0], accent_color[1], accent_color[2], 38))
    pill_mask = create_rounded_mask((pill_w, pill_h), radius=pill_h // 2)
    canvas.paste(pill_bg, (pill_x, pill_y), pill_mask)
    # Pill border
    draw.rounded_rectangle(
        [(pill_x, pill_y), (pill_x + pill_w, pill_y + pill_h)],
        radius=pill_h // 2,
        outline=(accent_color[0], accent_color[1], accent_color[2], 190),
        width=2
    )
    # Badge text
    draw.text(
        (pill_x + (pill_w - badge_tw) // 2, pill_y + (pill_h - badge_th) // 2 - 2),
        badge_text,
        font=font_badge,
        fill=(accent_color[0], accent_color[1], accent_color[2], 255)
    )

    # 4. Title Text (Crisp White)
    title_bbox = draw.textbbox((0, 0), title_text, font=font_title)
    title_tw = title_bbox[2] - title_bbox[0]
    title_x = (canvas_w - title_tw) // 2
    title_y = 108
    draw.text((title_x, title_y), title_text, font=font_title, fill=(255, 255, 255, 255))

    # 5. Subtitle Text (Muted Slate Gray)
    sub_bbox = draw.textbbox((0, 0), subtitle_text, font=font_sub)
    sub_tw = sub_bbox[2] - sub_bbox[0]
    sub_x = (canvas_w - sub_tw) // 2
    sub_y = 174
    draw.text((sub_x, sub_y), subtitle_text, font=font_sub, fill=(160, 174, 192, 255))

    # 6. Phone Device Mockup Construction - FULL DEVICE FIT
    raw_img = Image.open(raw_screenshot_path).convert('RGBA')
    raw_w, raw_h = raw_img.size
    crop_top = 105
    crop_bottom = 2340
    cropped_app = raw_img.crop((0, crop_top, raw_w, crop_bottom))
    crop_w, crop_h = cropped_app.size

    # Inner Screen dimensions strictly scaled to aspect ratio
    screen_h = 1584
    screen_w = int(screen_h * (crop_w / float(crop_h))) # ~765px
    bezel = 14
    phone_w = screen_w + (bezel * 2) # ~793px
    phone_h = screen_h + (bezel * 2) # ~1612px
    phone_radius = 48
    screen_radius = 36

    phone_x = (canvas_w - phone_w) // 2
    phone_y = 245 # Phone starts at 245, ends at 245 + 1612 = 1857 (leaving 63px bottom margin!)

    # Realistic Soft Drop Shadow behind phone (fully contained inside canvas)
    shadow_pad = 40
    shadow_img = Image.new('RGBA', (phone_w + shadow_pad * 2, phone_h + shadow_pad * 2), (0, 0, 0, 0))
    s_draw = ImageDraw.Draw(shadow_img)
    s_draw.rounded_rectangle(
        [(shadow_pad, shadow_pad + 10), (shadow_pad + phone_w, shadow_pad + phone_h + 10)],
        radius=phone_radius + 2,
        fill=(0, 0, 0, 200)
    )
    shadow_blurred = shadow_img.filter(ImageFilter.GaussianBlur(28))
    canvas.paste(shadow_blurred, (phone_x - shadow_pad, phone_y - shadow_pad), shadow_blurred)

    # Outer Phone Body (Dark Slate Metallic)
    phone_body = Image.new('RGBA', (phone_w, phone_h), (20, 24, 34, 255))
    p_draw = ImageDraw.Draw(phone_body)
    # Bezel outer and inner edge highlight
    p_draw.rounded_rectangle(
        [(1, 1), (phone_w - 2, phone_h - 2)],
        radius=phone_radius,
        outline=(52, 62, 78, 255),
        width=2
    )

    # Resize cropped app content to inner screen dimensions
    screen_img = cropped_app.resize((screen_w, screen_h), Image.Resampling.LANCZOS)
    screen_mask = create_rounded_mask((screen_w, screen_h), radius=screen_radius)
    phone_body.paste(screen_img, (bezel, bezel), screen_mask)

    # Punch-hole Camera
    cam_radius = 10
    cam_cx = phone_w // 2
    cam_cy = bezel + 20
    p_draw.ellipse(
        [(cam_cx - cam_radius, cam_cy - cam_radius), (cam_cx + cam_radius, cam_cy + cam_radius)],
        fill=(5, 5, 8, 255),
        outline=(30, 35, 45, 255),
        width=1
    )
    # Lens subtle glint
    p_draw.ellipse(
        [(cam_cx - 3, cam_cy - 3), (cam_cx - 1, cam_cy - 1)],
        fill=(45, 75, 120, 255)
    )

    # Phone mask for outer shape
    phone_mask = create_rounded_mask((phone_w, phone_h), radius=phone_radius)
    canvas.paste(phone_body, (phone_x, phone_y), phone_mask)

    # Save final PNG (RGB 24-bit no alpha, 100% Google Play compliant)
    final_rgb = canvas.convert('RGB')
    final_rgb.save(output_path, 'PNG', quality=95)
    print(f'Successfully generated: {output_path} (Bottom y: {phone_y + phone_h} / {canvas_h})')

def main():
    assets_dir = r'c:\Users\Salih\StudioProjects\MovieAppCompose\play_store_assets'
    artifact_dir = r'C:\Users\Salih\.gemini\antigravity\brain\cc2642d3-3852-42fb-a0fb-27e1780cd45f'

    configs = [
        {
            'raw': 'Screenshot_20261108_004513.png',
            'out': 'store_screenshot_1.png',
            'badge': 'KEŞFET & TRENDLER',
            'title': 'Popüler Filmleri Anında Keşfet',
            'sub': 'En çok izlenen vizyon filmleri ve güncel trendler elinin altında',
            'color': (239, 68, 68) # Red
        },
        {
            'raw': 'Screenshot_20261108_004603.png',
            'out': 'store_screenshot_2.png',
            'badge': 'FİLM DETAYLARI',
            'title': 'Kapsamlı Bilgiler ve Oyuncular',
            'sub': 'IMDb puanları, film konusu ve oyuncu kadrosu tek ekranda',
            'color': (245, 158, 11) # Amber Gold
        },
        {
            'raw': 'Screenshot_20261108_004830.png',
            'out': 'store_screenshot_3.png',
            'badge': 'HIZLI ARAMA',
            'title': 'Aradığın Yapımı Kolayca Bul',
            'sub': 'Binlerce film arasında saniyeler içinde anında arama yap',
            'color': (14, 165, 233) # Sky Blue
        },
        {
            'raw': 'Screenshot_20261108_011525.png',
            'out': 'store_screenshot_4.png',
            'badge': 'İZLEME LİSTESİ',
            'title': 'Favorilerini Kaydet ve Yönet',
            'sub': 'Beğendiğin yapımları listene ekle, hiçbir filmi kaçırma',
            'color': (168, 85, 247) # Purple
        },
        {
            'raw': 'Screenshot_20261108_011546.png',
            'out': 'store_screenshot_5.png',
            'badge': 'KATEGORİLER & VİZYON',
            'title': 'Tüm Türler ve Vizyondakiler',
            'sub': 'Aksiyon, komedi, bilim kurgu ve vizyondaki tüm filmler',
            'color': (16, 185, 129) # Emerald
        }
    ]

    for item in configs:
        raw_p = os.path.join(assets_dir, item['raw'])
        out_p = os.path.join(assets_dir, item['out'])
        render_store_screenshot(
            raw_p,
            out_p,
            item['badge'],
            item['title'],
            item['sub'],
            accent_color=item['color']
        )
        art_p = os.path.join(artifact_dir, item['out'])
        shutil.copyfile(out_p, art_p)
        print(f'Copied to artifact dir: {art_p}')

if __name__ == '__main__':
    main()
