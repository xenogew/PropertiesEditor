import os
import urllib.request
import subprocess

lucide_base = "https://unpkg.com/lucide-static@latest/icons/{}.svg"

icons_map = {
    # icons/
    "icons/alphab_sort_co-x.png": ("arrow-down-a-z", 16, "#333333"),
    "icons/editPage-x.png": ("file-pen-line", 16, "#333333"),
    "icons/IconByTaroTw_disable-x.png": ("file-cog", 16, "#999999"),
    "icons/IconByTaroTw-x.png": ("file-cog", 16, "#2563eb"),
    "icons/outlineMarker-x.png": ("list-tree", 16, "#333333"),
    "icons/pe_32-x.png": ("file-cog", 32, "#2563eb"),
    "icons/previewPage-x.png": ("eye", 16, "#333333"),
    "icons/propedit_marker-x.png": ("message-square-warning", 16, "#eab308"),
    "icons/psearch_obj-x.png": ("search", 16, "#333333"),
    
    # resources
    "src/io/github/xenogew/propedit/resource/ColumnInsertBefore16-x.png": ("table-columns-split", 16, "#333333"),
    "src/io/github/xenogew/propedit/resource/Copy16-x.png": ("copy", 16, "#333333"),
    "src/io/github/xenogew/propedit/resource/Cut16-x.png": ("scissors", 16, "#333333"),
    "src/io/github/xenogew/propedit/resource/Delete16-x.png": ("trash-2", 16, "#ef4444"),
    "src/io/github/xenogew/propedit/resource/Find16-x.png": ("search", 16, "#333333"),
    "src/io/github/xenogew/propedit/resource/FindAgain16-x.png": ("search-check", 16, "#333333"),
    "src/io/github/xenogew/propedit/resource/Information16-x.png": ("info", 16, "#3b82f6"),
    "src/io/github/xenogew/propedit/resource/New16-x.png": ("file-plus-2", 16, "#333333"),
    "src/io/github/xenogew/propedit/resource/Open16-x.png": ("folder-open", 16, "#333333"),
    "src/io/github/xenogew/propedit/resource/Paste16-x.png": ("clipboard", 16, "#333333"),
    "src/io/github/xenogew/propedit/resource/Replace16-x.png": ("replace", 16, "#333333"),
    "src/io/github/xenogew/propedit/resource/Save16-x.png": ("save", 16, "#333333"),
    "src/io/github/xenogew/propedit/resource/Stop16-x.png": ("circle-stop", 16, "#ef4444"),
    "src/io/github/xenogew/propedit/resource/Undo16-x.png": ("undo-2", 16, "#333333"),
    "src/io/github/xenogew/propedit/resource/editor-x.png": ("file-cog", 16, "#2563eb"),
    "src/io/github/xenogew/propedit/resource/pe_16-x.png": ("file-cog", 16, "#2563eb"),
    "src/io/github/xenogew/propedit/resource/pe_32-x.png": ("file-cog", 32, "#2563eb"),
}

for out_path, (icon_name, size, color) in icons_map.items():
    url = lucide_base.format(icon_name)
    try:
        req = urllib.request.urlopen(url)
        svg_content = req.read().decode('utf-8')
        
        # Replace currentColor with our desired color
        svg_content = svg_content.replace('currentColor', color)
        
        svg_path = out_path + ".svg"
        with open(svg_path, "w") as f:
            f.write(svg_content)
            
        # Convert to PNG
        # SVG density helps convert not rasterize too blurrily
        # convert -background none -density 300 -resize {size}x{size} {svg_path} {out_path}
        cmd = ["convert", "-background", "none", "-density", "300", "-resize", f"{size}x{size}", svg_path, out_path]
        subprocess.run(cmd, check=True)
        
        # Clean up svg
        os.remove(svg_path)
        print(f"Generated {out_path}")
        
    except Exception as e:
        print(f"Failed {icon_name}: {e}")

