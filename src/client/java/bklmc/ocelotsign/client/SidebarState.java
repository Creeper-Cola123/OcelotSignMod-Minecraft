package bklmc.ocelotsign.client;

/**
 * 侧边栏顶层项互斥选择逻辑
 *
 * @see PatternAndFontOverlay
 */
public final class SidebarState {

    /** 无选中项 */
    public static final int SIDEBAR_NONE = -1;
    /** 文档列表 */
    public static final int SIDEBAR_DOCS = 0;
    /** 调色板 */
    public static final int SIDEBAR_PALETTE = 1;
    /** 颜色拾取器 */
    public static final int SIDEBAR_PICKER = 2;
    /** 致谢 */
    public static final int SIDEBAR_ACK = 3;

    private SidebarState() {
    }

    /**
     * 统一设置侧边栏顶层项并强制互斥。
     *
     * <p>清空滚动位置，清理 H2/H3 选中状态。
     *
     * @param which 顶层项索引
     */
    public static void selectSidebarTop(int which) {
        PatternAndFontOverlay.sidebarSelection = which;
        PatternAndFontOverlay.isDocumentListSelected = (which == SIDEBAR_DOCS);
        PatternAndFontOverlay.isColorPaletteSelected = (which == SIDEBAR_PALETTE);
        PatternAndFontOverlay.isColorPickerSelected  = (which == SIDEBAR_PICKER);
        PatternAndFontOverlay.isAcknowledgmentSelected = (which == SIDEBAR_ACK);
        if (which != SIDEBAR_NONE) {
            PatternAndFontOverlay.selectedH2 = null;
            PatternAndFontOverlay.selectedH3 = null;
        }
        PatternAndFontOverlay.scrollY = 0;
    }

    /**
     * 选中 H2/H3 分类时清空四个顶层项标志。
     */
    public static void clearSidebarTop() {
        PatternAndFontOverlay.sidebarSelection = SIDEBAR_NONE;
        PatternAndFontOverlay.isDocumentListSelected = false;
        PatternAndFontOverlay.isColorPaletteSelected = false;
        PatternAndFontOverlay.isColorPickerSelected = false;
        PatternAndFontOverlay.isAcknowledgmentSelected = false;
    }

    /**
     * 防御性自愈：确保顶层项互斥。
     *
     * <p>每帧渲染前调用，优先级：ack &gt; picker &gt; palette &gt; docs。
     * 若 selectedH2/H3 与顶层项同时存在，优先保留顶层项。
     */
    public static void enforceSidebarMutualExclusion() {
        int count = (PatternAndFontOverlay.isDocumentListSelected ? 1 : 0)
                  + (PatternAndFontOverlay.isColorPaletteSelected ? 1 : 0)
                  + (PatternAndFontOverlay.isColorPickerSelected ? 1 : 0)
                  + (PatternAndFontOverlay.isAcknowledgmentSelected ? 1 : 0);
        if (count > 1) {
            PatternAndFontOverlay.isDocumentListSelected = PatternAndFontOverlay.isDocumentListSelected
                    && !PatternAndFontOverlay.isColorPaletteSelected
                    && !PatternAndFontOverlay.isColorPickerSelected
                    && !PatternAndFontOverlay.isAcknowledgmentSelected;
            PatternAndFontOverlay.isColorPaletteSelected = PatternAndFontOverlay.isColorPaletteSelected
                    && !PatternAndFontOverlay.isColorPickerSelected
                    && !PatternAndFontOverlay.isAcknowledgmentSelected;
            PatternAndFontOverlay.isColorPickerSelected = PatternAndFontOverlay.isColorPickerSelected
                    && !PatternAndFontOverlay.isAcknowledgmentSelected;
        } else if (count == 0
                && PatternAndFontOverlay.sidebarSelection != SIDEBAR_NONE
                && (PatternAndFontOverlay.selectedH2 == null && PatternAndFontOverlay.selectedH3 == null)) {
            PatternAndFontOverlay.isDocumentListSelected = (PatternAndFontOverlay.sidebarSelection == SIDEBAR_DOCS);
            PatternAndFontOverlay.isColorPaletteSelected = (PatternAndFontOverlay.sidebarSelection == SIDEBAR_PALETTE);
            PatternAndFontOverlay.isColorPickerSelected  = (PatternAndFontOverlay.sidebarSelection == SIDEBAR_PICKER);
            PatternAndFontOverlay.isAcknowledgmentSelected = (PatternAndFontOverlay.sidebarSelection == SIDEBAR_ACK);
        }

        if (count > 0) {
            PatternAndFontOverlay.selectedH2 = null;
            PatternAndFontOverlay.selectedH3 = null;
        }
    }
}
