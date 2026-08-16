package bklmc.ocelotsign.client;

import bklmc.ocelotsign.OcelotSignMod;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图案与字体分类注册中心
 *
 * @see PatternAndFontOverlay
 */
public final class PatternRegistry {

    private PatternRegistry() {
    }

    /**
     * 注册内置图案分类并构建纹理缓存。幂等，仅首次调用生效。
     */
    public static void registerBuiltInPatterns() {
        if (PatternAndFontOverlay.isDataLoaded) return;

        PatternAndFontOverlay.initMishangPatterns();
        PatternAndFontOverlay.H2Category patternCategory = new PatternAndFontOverlay.H2Category(Text.translatable("ocelotsignmod.gui.tabs.patterns"));

        PatternAndFontOverlay.H3Category mishangCategory = new PatternAndFontOverlay.H3Category(Text.translatable("ocelotsignmod.gui.categories.mishang_builtin"));
        mishangCategory.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.rect"),
                Text.translatable("ocelotsignmod.gui.sections.rect.desc"),
                Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/"))
                .setWhitelistMode()
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/rect.png"),
                        "-rect <长> <宽>", Text.translatable("ocelotsignmod.gui.pattern.rect")));

        mishangCategory.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.mishang_patterns"),
                Text.translatable("ocelotsignmod.gui.sections.mishang_patterns.desc"),
                Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/"))
                .setWhitelistMode()
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-left.png"), "-pattern al", Text.translatable("ocelotsignmod.gui.pattern.arrow_left"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-right.png"), "-pattern ar", Text.translatable("ocelotsignmod.gui.pattern.arrow_right"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-up.png"), "-pattern au", Text.translatable("ocelotsignmod.gui.pattern.arrow_up"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-down.png"), "-pattern ad", Text.translatable("ocelotsignmod.gui.pattern.arrow_down"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-left-thin.png"), "-pattern arrow-left-thin", Text.translatable("ocelotsignmod.gui.pattern.arrow_left_thin"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-right-thin.png"), "-pattern arrow-right-thin", Text.translatable("ocelotsignmod.gui.pattern.arrow_right_thin"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-up-thin.png"), "-pattern arrow-up-thin", Text.translatable("ocelotsignmod.gui.pattern.arrow_up_thin"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-down-thin.png"), "-pattern arrow-down-thin", Text.translatable("ocelotsignmod.gui.pattern.arrow_down_thin"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-left-up.png"), "-pattern alu", Text.translatable("ocelotsignmod.gui.pattern.arrow_left_up"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-right-up.png"), "-pattern aru", Text.translatable("ocelotsignmod.gui.pattern.arrow_right_up"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-left-down.png"), "-pattern ald", Text.translatable("ocelotsignmod.gui.pattern.arrow_left_down"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-right-down.png"), "-pattern ard", Text.translatable("ocelotsignmod.gui.pattern.arrow_right_down"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-left-turn-up.png"), "-pattern altu", Text.translatable("ocelotsignmod.gui.pattern.arrow_left_turn_up"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-right-turn-up.png"), "-pattern artu", Text.translatable("ocelotsignmod.gui.pattern.arrow_right_turn_up"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-left-turn-down.png"), "-pattern altd", Text.translatable("ocelotsignmod.gui.pattern.arrow_left_turn_down"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-right-turn-down.png"), "-pattern artd", Text.translatable("ocelotsignmod.gui.pattern.arrow_right_turn_down"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-left-right.png"), "-pattern alr", Text.translatable("ocelotsignmod.gui.pattern.arrow_left_right"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/arrow-up-down.png"), "-pattern aud", Text.translatable("ocelotsignmod.gui.pattern.arrow_up_down"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/circle-small.png"), "-pattern circle-small", Text.translatable("ocelotsignmod.gui.pattern.circle_small"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/circle-medium.png"), "-pattern O", Text.translatable("ocelotsignmod.gui.pattern.circle_medium"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/ban.png"), "-pattern ban", Text.translatable("ocelotsignmod.gui.pattern.ban"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/u-turn-left-down.png"), "-pattern uld", Text.translatable("ocelotsignmod.gui.pattern.u_turn_left_down"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/u-turn-right-down.png"), "-pattern urd", Text.translatable("ocelotsignmod.gui.pattern.u_turn_right_down"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/u-turn-left-up.png"), "-pattern ulu", Text.translatable("ocelotsignmod.gui.pattern.u_turn_left_up"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/u-turn-right-up.png"), "-pattern uru", Text.translatable("ocelotsignmod.gui.pattern.u_turn_right_up"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/cross-small.png"), "-pattern cross-small", Text.translatable("ocelotsignmod.gui.pattern.cross_small"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/cross-medium.png"), "-pattern X", Text.translatable("ocelotsignmod.gui.pattern.cross_medium"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/cross-large.png"), "-pattern cross-large", Text.translatable("ocelotsignmod.gui.pattern.cross_large"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/square-small.png"), "-pattern square-small", Text.translatable("ocelotsignmod.gui.pattern.square_small"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/square-medium.png"), "-pattern square", Text.translatable("ocelotsignmod.gui.pattern.square_medium"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/square-large.png"), "-pattern square-large", Text.translatable("ocelotsignmod.gui.pattern.square_large"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/square-slant-small.png"), "-pattern small-slant-square", Text.translatable("ocelotsignmod.gui.pattern.square_slant_small"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/square-slant-medium.png"), "-pattern medium-slant-square", Text.translatable("ocelotsignmod.gui.pattern.square_slant_medium"))
                .addWhitelistItem(Identifier.of("ocelotsignmod", "textures/mishanguc_patterns/square-slant-large.png"), "-pattern large-slant-square", Text.translatable("ocelotsignmod.gui.pattern.square_slant_large")));

        PatternAndFontOverlay.H3Category modCategory = new PatternAndFontOverlay.H3Category(Text.translatable("ocelotsignmod.gui.categories.ocelot_builtin"));

        PatternAndFontOverlay.H3Category roadSigns = new PatternAndFontOverlay.H3Category(Text.translatable("ocelotsignmod.gui.categories.road_signs_cn"));
        addRoadSignSections(roadSigns);
        modCategory.addSubCategory(roadSigns);

        PatternAndFontOverlay.H3Category publicSigns = new PatternAndFontOverlay.H3Category(Text.translatable("ocelotsignmod.gui.categories.public_signs"));
        addPublicSignSections(publicSigns);
        modCategory.addSubCategory(publicSigns);

        PatternAndFontOverlay.H3Category customPatternCategory = new PatternAndFontOverlay.H3Category(Text.translatable("ocelotsignmod.gui.categories.custom_resource_pack"));
        customPatternCategory.headerText = Text.translatable("ocelotsignmod.gui.sections.custom_patterns.desc");
        customPatternCategory.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.custom_patterns"),
                Text.literal(""), Identifier.of("ocelotsignmod", "patterns/"))
                .setCustomJsonPath("ocelotsignmod:patterns/custom_patterns.json"));

        patternCategory.addSubCategory(mishangCategory);
        patternCategory.addSubCategory(modCategory);
        patternCategory.addSubCategory(customPatternCategory);

        PatternAndFontOverlay.H2Category fontCategory = new PatternAndFontOverlay.H2Category(Text.translatable("ocelotsignmod.gui.tabs.fonts"));

        PatternAndFontOverlay.H3Category builtInFonts = new PatternAndFontOverlay.H3Category(Text.translatable("ocelotsignmod.gui.categories.ocelot_builtin"));
        builtInFonts.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.default_fonts"),
                Text.translatable("ocelotsignmod.gui.sections.default_fonts.desc"),
                Identifier.of("ocelotsignmod", "textures/font/default/"))
                .setFontMode()
                .addFontItem("ocelotsignmod:traf_sign_font_a", Text.translatable("ocelotsignmod.gui.fonts.traf_sign_font_a"))
                .addFontItem("ocelotsignmod:traf_sign_font_b", Text.translatable("ocelotsignmod.gui.fonts.traf_sign_font_b"))
                .addFontItem("ocelotsignmod:traf_sign_font_c", Text.translatable("ocelotsignmod.gui.fonts.traf_sign_font_c"))
                .addFontItem("ocelotsignmod:heiti", Text.translatable("ocelotsignmod.gui.fonts.heiti"))
                .addFontItem("ocelotsignmod:songti", Text.translatable("ocelotsignmod.gui.fonts.songti"))
                .addFontItem("ocelotsignmod:misans_semibold", Text.translatable("ocelotsignmod.gui.fonts.misans_semibold")));

        PatternAndFontOverlay.H3Category customFontCategory = new PatternAndFontOverlay.H3Category(Text.translatable("ocelotsignmod.gui.categories.custom_resource_pack"));
        customFontCategory.headerText = Text.translatable("ocelotsignmod.gui.sections.custom_fonts.desc");
        customFontCategory.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.custom_fonts"),
                Text.literal(""), Identifier.of("ocelotsignmod", "fonts/"))
                .setFontMode()
                .setCustomJsonPath("ocelotsignmod:fonts/custom_fonts.json"));

        fontCategory.addSubCategory(builtInFonts);
        fontCategory.addSubCategory(customFontCategory);

        PatternAndFontOverlay.REGISTRY.add(patternCategory);
        PatternAndFontOverlay.REGISTRY.add(fontCategory);

        setDefaultSelection();
        loadAllFromResourceManager(MinecraftClient.getInstance().getResourceManager());

        PatternAndFontOverlay.isDataLoaded = true;
    }

    // 设置默认选中第一个叶子 H3 节点
    private static void setDefaultSelection() {
        if (PatternAndFontOverlay.REGISTRY.isEmpty()) return;
        PatternAndFontOverlay.selectedH2 = PatternAndFontOverlay.REGISTRY.get(0);
        if (!PatternAndFontOverlay.selectedH2.subCategories.isEmpty()) {
            PatternAndFontOverlay.selectedH3 = findFirstLeafH3(PatternAndFontOverlay.selectedH2.subCategories.get(0));
        }
    }

    // 从资源管理器加载所有分类数据
    private static void loadAllFromResourceManager(ResourceManager manager) {
        for (PatternAndFontOverlay.H2Category h2 : PatternAndFontOverlay.REGISTRY) {
            for (PatternAndFontOverlay.H3Category h3 : h2.subCategories) {
                buildTextureCacheForH3(h3, manager);
            }
        }

        loadCustomPatternsFromJson(manager);
        loadCustomFontsFromJson(manager);
        loadAdvancedCustomUIFromJson(manager);
    }

    // 添加道路交通标志分区
    private static void addRoadSignSections(PatternAndFontOverlay.H3Category parent) {
        Identifier basicPath = Identifier.of("ocelotsignmod", "textures/sign/road/basic/");
        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.basic"), Text.literal(""), basicPath)
                .enableSubfolders()
                .addSubFolder("black", Text.translatable("ocelotsignmod.gui.colors.black"))
                .addSubFolder("white", Text.translatable("ocelotsignmod.gui.colors.white"))
                .addSubFolder("blue", Text.translatable("ocelotsignmod.gui.colors.blue"))
                .addSubFolder("green", Text.translatable("ocelotsignmod.gui.colors.green"))
                .addSubFolder("yellow", Text.translatable("ocelotsignmod.gui.colors.yellow"))
                .addSubFolder("brown", Text.translatable("ocelotsignmod.gui.colors.brown")));

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.arrows"), Text.literal(""),
                Identifier.of("ocelotsignmod", "textures/sign/road/arrowsign/"))
                .enableSubfolders()
                .addSubFolder("black", Text.translatable("ocelotsignmod.gui.colors.black"))
                .addSubFolder("white", Text.translatable("ocelotsignmod.gui.colors.white"))
                .addSubFolder("blue", Text.translatable("ocelotsignmod.gui.colors.blue"))
                .addSubFolder("green", Text.translatable("ocelotsignmod.gui.colors.green"))
                .addSubFolder("yellow", Text.translatable("ocelotsignmod.gui.colors.yellow")));

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.direction_signs"), Text.literal(""),
                Identifier.of("ocelotsignmod", "textures/sign/road/directionsign/"))
                .setExtensionFilter(PatternAndFontOverlay.FilterMode.BLACKLIST, "arrow1.png", "arrow2.png", "arrow3.png"));

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.restriction_signs"), Text.literal(""),
                Identifier.of("ocelotsignmod", "textures/sign/road/")));

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.road_number"), Text.literal(""),
                Identifier.of("ocelotsignmod", "textures/sign/road/roadnumber/")));

        addColoredSignSections(parent, "facility_signs", "textures/sign/road/facilities/",
                new String[]{"black", "white", "blue", "green"});
        addColoredSignSections(parent, "road_users", "textures/sign/road/roaduser/",
                new String[]{"black", "white", "blue", "green"});

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.warning_signs"), Text.literal(""),
                Identifier.of("ocelotsignmod", "textures/sign/road/warningsign/")));

        addColoredSignSections(parent, "cropped_warning_signs", "textures/sign/road/croppedwarningsign/",
                new String[]{"black", "white", "blue", "green"});

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.guide_signs"), Text.literal(""),
                Identifier.of("ocelotsignmod", "textures/sign/road/guidesign/")));

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.prohibition_signs"), Text.literal(""),
                Identifier.of("ocelotsignmod", "textures/sign/road/prohibitsign/")));

        addColoredSignSections(parent, "scenic_area_signs", "textures/sign/road/scenicarea/",
                new String[]{"black", "white", "brown"});
        addColoredSignSections(parent, "driver_action", "textures/sign/road/driveraction/",
                new String[]{"black", "white", "blue", "green"});

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.misc_signs"), Text.literal(""),
                Identifier.of("ocelotsignmod", "textures/sign/road/extra/")));

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.road_arrow_style_1"), Text.translatable("ocelotsignmod.gui.sections.road_arrow_style_1.desc"),
                Identifier.of("ocelotsignmod", "textures/block/roadmark_large/")));

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.road_arrow_style_2"), Text.translatable("ocelotsignmod.gui.sections.road_arrow_style_2.desc"),
                Identifier.of("ocelotsignmod", "textures/block/roadmark_style_2/")));

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.road_arrow_style_3"), Text.translatable("ocelotsignmod.gui.sections.road_arrow_style_3.desc"),
                Identifier.of("ocelotsignmod", "textures/block/roadmark_style_3/")));

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.road_mark_lines"), Text.literal(""), Identifier.of("mishanguc", "textures/block/")).setExtensionFilter(PatternAndFontOverlay.FilterMode.WHITELIST, "white_and_yellow_double_right_angle_line.png", "white_and_yellow_right_angle_line.png", "white_auto_bevel_angle_line.png", "white_auto_right_angle_line.png", "white_bevel_angle_double_line.png", "white_bevel_angle_line.png", "white_bevel_angle_thick_line.png", "white_bi_bevel_angle_line_to_straight_double_line.png", "white_cross_line.png", "white_double_joint_line.png", "white_double_joint_line_with_offset_side.png", "white_half_double_line.png", "white_ink.png", "white_joint_line.png", "white_joint_line_with_double_side.png", "white_joint_line_with_offset_side.png", "white_joint_line_with_offset_yellow_side.png", "white_joint_line_with_thick_side.png", "white_joint_line_with_yellow_double_side.png", "white_joint_line_with_yellow_side.png", "white_light.png", "white_light_emission.png", "white_offset_in_bevel_angle_line.png", "white_offset_in_right_angle_line.png", "white_offset_out_bevel_angle_line.png", "white_offset_out_right_angle_line.png", "white_offset_straight_line.png", "white_offset_straight_line2.png", "white_pure.png", "white_right_angle_line.png", "white_right_angle_line_with_one_part_offset_in.png", "white_right_angle_line_with_one_part_offset_out.png", "white_straight_double_line.png", "white_straight_line.png", "white_straight_thick_line.png", "white_thick_and_normal_right_angle_line.png", "white_thick_and_yellow_double_right_angle_line.png", "white_thick_and_yellow_right_angle_line.png", "white_thick_joint_line.png", "white_thick_joint_line_with_offset_side.png", "white_thick_joint_line_with_offset_yellow_side.png", "white_thick_joint_line_with_yellow_double_side.png", "white_thick_joint_line_with_yellow_side.png", "white_unknown_line.png", "white_yellow_double_straight_line.png", "yellow_bevel_angle_double_line.png", "yellow_bevel_angle_line.png", "yellow_bevel_angle_thick_line.png", "yellow_cross_line.png", "yellow_half_double_line.png", "yellow_ink.png", "yellow_joint_line.png", "yellow_joint_line_with_offset_side.png", "yellow_joint_line_with_offset_yellow_side.png", "yellow_joint_line_with_white_side.png", "yellow_light.png", "yellow_offset_in_bevel_angle_line.png", "yellow_offset_in_right_angle_line.png", "yellow_offset_out_bevel_angle_line.png", "yellow_offset_out_right_angle_line.png", "yellow_offset_straight_line.png", "yellow_offset_straight_line2.png", "yellow_right_angle_line.png", "yellow_right_angle_line_with_one_part_offset_in.png", "yellow_right_angle_line_with_one_part_offset_out.png", "yellow_straight_double_line.png", "yellow_straight_line.png", "yellow_straight_thick_line.png"));
    }

    // 添加公共场所标志分区
    private static void addPublicSignSections(PatternAndFontOverlay.H3Category parent) {
        Identifier hangingSignPath = Identifier.of("ocelotsignmod", "textures/sign/hangingsign/");
        String[] colors = new String[]{"black", "white", "yellow"};

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.arrows"), Text.literal(""), hangingSignPath)
                .enableSubfolders()
                .addSubFolder("black", Text.translatable("ocelotsignmod.gui.colors.black"))
                .addSubFolder("white", Text.translatable("ocelotsignmod.gui.colors.white"))
                .addSubFolder("yellow", Text.translatable("ocelotsignmod.gui.colors.yellow"))
                .setExtensionFilter(PatternAndFontOverlay.FilterMode.WHITELIST, "arrow1.png", "arrow2.png", "arrow3.png", "arrow4.png", "arrow5.png", "arrow6.png", "arrow7.png"));

        parent.addSection(new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections.basic_shapes"), Text.literal(""), hangingSignPath)
                .enableSubfolders()
                .addSubFolder("black", Text.translatable("ocelotsignmod.gui.colors.black"))
                .addSubFolder("white", Text.translatable("ocelotsignmod.gui.colors.white"))
                .addSubFolder("yellow", Text.translatable("ocelotsignmod.gui.colors.yellow"))
                .setExtensionFilter(PatternAndFontOverlay.FilterMode.WHITELIST, "bg1.png", "bg2.png", "bg3.png"));

        addWhitelistSignSection(parent, "traffic", hangingSignPath, colors,
                "train1.png", "train2.png", "train3.png", "transfer1.png", "connecting_train1.png", "crh_transfer1.png", "rail1.png", "plane1.png", "departures1.png", "arrivals1.png", "connecting_flight1.png", "helicopter1.png", "light_rail1.png", "maglev1.png", "monorail1.png", "cable_railway1.png", "cable_car1.png", "car1.png", "bicycle1.png", "bus1.png", "ride_hailing_car1.png", "ship1.png", "streetcar1.png", "taxi1.png", "trolleybus1.png", "coveredparkinglot1.png", "parkinglot1.png");

        addWhitelistSignSection(parent, "ticketing_security", hangingSignPath, colors,
                "customs1.png", "security1.png", "security_check1.png", "quarantine1.png", "tickets1.png", "tickets2.png", "ticket_check1.png", "ticket_office1.png", "currency_exchange1.png");

        addWhitelistSignSection(parent, "baggage_service", hangingSignPath, colors,
                "baggage1.png", "baggage_cart1.png", "baggage_check_in1.png", "baggage_check1.png", "baggage_claim1.png", "left_luggage1.png", "lost_property1.png");

        addWhitelistSignSection(parent, "public_facilities", hangingSignPath, colors,
                "toilet1.png", "men1.png", "women1.png", "nursing_room1.png", "information1.png", "asks1.png", "first_class_lounge1.png", "business_class_lounge1.png", "lounge1.png", "meeting_point1.png", "accommodation1.png", "hotel1.png", "hospital1.png", "telephone1.png", "reception1.png", "automatic_teller_machine1.png", "police_office1.png", "library1.png", "gymnasium1.png", "stadium1.png", "bank1.png", "cashier1.png", "cloak_room1.png", "conference_room1.png", "gas_station1.png", "lecture_hall1.png", "automatic_vending_mashine1.png", "book_and_news1.png", "very_important_person1.png");

        addWhitelistSignSection(parent, "food_shopping", hangingSignPath, colors,
                "chinese_restaurant1.png", "western_restaurant1.png", "coffee1.png", "tea1.png", "snack1.png", "bar1.png", "drinking_water1.png", "shopping_area1.png", "supermarket1.png", "smoking_area1.png");

        addWhitelistSignSection(parent, "building_access", hangingSignPath, colors,
                "accessible_elevator1.png", "elevator1.png", "entrance1.png", "exit1.png", "escalator1.png", "escalator2.png", "escalator3.png", "stairs1.png", "stairs2.png", "stairs3.png", "wheelchair_accessible1.png", "wheelchair_accessible2.png", "wheelchair_accessible3.png", "wheelchair_accessible4.png");
    }

    // 添加带颜色子文件夹的分区
    private static void addColoredSignSections(PatternAndFontOverlay.H3Category parent, String sectionKey, String basePath, String[] colors) {
        PatternAndFontOverlay.H4Section section = new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections." + sectionKey), Text.literal(""),
                Identifier.of("ocelotsignmod", basePath));
        section.enableSubfolders();
        for (String color : colors) {
            section.addSubFolder(color, Text.translatable("ocelotsignmod.gui.colors." + color));
        }
        parent.addSection(section);
    }

    // 添加带白名单过滤的颜色子文件夹分区
    private static void addWhitelistSignSection(PatternAndFontOverlay.H3Category parent, String sectionKey, Identifier basePath, String[] colors, String... whitelistFiles) {
        PatternAndFontOverlay.H4Section section = new PatternAndFontOverlay.H4Section(Text.translatable("ocelotsignmod.gui.sections." + sectionKey), Text.literal(""), basePath);
        section.enableSubfolders();
        for (String color : colors) {
            section.addSubFolder(color, Text.translatable("ocelotsignmod.gui.colors." + color));
        }
        section.setExtensionFilter(PatternAndFontOverlay.FilterMode.WHITELIST, whitelistFiles);
        parent.addSection(section);
    }

    // 递归构建 H3 分类下的纹理缓存
    private static void buildTextureCacheForH3(PatternAndFontOverlay.H3Category h3, ResourceManager manager) {
        for (PatternAndFontOverlay.H4Section h4 : h3.sections) {
            buildTextureCache(h4, manager);
        }
        for (PatternAndFontOverlay.H3Category child : h3.subCategories) {
            buildTextureCacheForH3(child, manager);
        }
    }

    /**
     * 根据当前 MinecraftClient 实例的资源管理器扫描并分类 H4Section 的纹理。
     *
     * @param section 目标 Section
     */
    public static void buildTextureCache(PatternAndFontOverlay.H4Section section) {
        buildTextureCache(section, MinecraftClient.getInstance().getResourceManager());
    }

    /**
     * 根据 H4Section 的配置扫描资源包并按子文件夹/过滤器分类纹理。
     *
     * @param section 目标 Section
     * @param resourceManager 资源管理器
     */
    public static void buildTextureCache(PatternAndFontOverlay.H4Section section, ResourceManager resourceManager) {
        section.cachedTextures.clear();

        String targetNamespace = section.basePath.getNamespace();
        String targetPath = section.basePath.getPath();

        if (targetPath.endsWith("/")) {
            targetPath = targetPath.substring(0, targetPath.length() - 1);
        }

        Map<Identifier, Resource> allResources = resourceManager.findResources(targetPath,
                id -> id.getPath().endsWith(".png"));

        initCacheBuckets(section);

        for (Identifier id : allResources.keySet()) {
            if (!id.getNamespace().equals(targetNamespace)) continue;

            String fullPath = id.getPath();
            if (!fullPath.startsWith(targetPath)) continue;

            String fileName = fullPath.substring(fullPath.lastIndexOf('/') + 1);
            if (!passesExtensionFilter(section, fileName)) continue;

            String relativePath = fullPath.substring(targetPath.length());
            if (relativePath.startsWith("/")) relativePath = relativePath.substring(1);

            distributeToBucket(section, id, relativePath);
        }

        for (List<Identifier> list : section.cachedTextures.values()) {
            list.sort((id1, id2) -> id1.getPath().compareTo(id2.getPath()));
        }
    }

    // 初始化缓存桶
    private static void initCacheBuckets(PatternAndFontOverlay.H4Section section) {
        if (section.useSubfolders && !section.subFolders.isEmpty()) {
            for (PatternAndFontOverlay.SubFolderDef subDef : section.subFolders) {
                section.cachedTextures.put(subDef.dirName, new ArrayList<>());
            }
        } else {
            section.cachedTextures.put("root", new ArrayList<>());
        }
    }

    // 判断文件名是否通过扩展名过滤
    private static boolean passesExtensionFilter(PatternAndFontOverlay.H4Section section, String fileName) {
        if (section.extFilterMode == PatternAndFontOverlay.FilterMode.WHITELIST) {
            return section.extFilterList.stream().anyMatch(fileName::equals);
        }
        if (section.extFilterMode == PatternAndFontOverlay.FilterMode.BLACKLIST) {
            return section.extFilterList.stream().noneMatch(fileName::equals);
        }
        return true;
    }

    // 将纹理分配到缓存桶
    private static void distributeToBucket(PatternAndFontOverlay.H4Section section, Identifier id, String relativePath) {
        String[] pathSegments = relativePath.split("/");
        if (section.useSubfolders) {
            if (pathSegments.length == 2 && section.cachedTextures.containsKey(pathSegments[0])) {
                section.cachedTextures.get(pathSegments[0]).add(id);
            }
        } else {
            if (pathSegments.length == 1 && section.cachedTextures.containsKey("root")) {
                section.cachedTextures.get("root").add(id);
            }
        }
    }

    // 从 JSON 加载自定义图案
    private static void loadCustomPatternsFromJson(ResourceManager manager) {
        forEachCustomResourceSection(h2 -> h2.subCategories, "ocelotsignmod.gui.tabs.patterns",
                section -> loadCustomPatternsFromJsonSection(section, manager));
    }

    // 解析自定义图案 JSON 并追加到指定 Section
    private static void loadCustomPatternsFromJsonSection(PatternAndFontOverlay.H4Section section, ResourceManager manager) {
        Identifier jsonId = Identifier.of(section.customJsonPath);

        try {
            List<Resource> resources = collectAllResources(manager, jsonId);
            for (Resource resource : resources) {
                try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                    JsonElement root = JsonParser.parseReader(reader);
                    if (root.isJsonArray()) {
                        for (JsonElement element : root.getAsJsonArray()) {
                            JsonObject obj = element.getAsJsonObject();
                            String name = obj.has("name") ? obj.get("name").getAsString() : Text.translatable("ocelotsignmod.gui.unnamed").getString();
                            String texture = obj.has("texture") ? obj.get("texture").getAsString() : "";
                            String insert = obj.has("insert") ? obj.get("insert").getAsString() : "";

                            if (!texture.isEmpty()) {
                                section.addWhitelistItem(Identifier.of(texture), insert, Text.literal(name));
                            }
                        }
                    }
                } catch (Exception innerE) {
                    System.err.println("[PatternRegistry] Failed to parse custom pattern JSON: " + innerE.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("[PatternRegistry] Custom pattern JSON 读取异常: " + e.getMessage());
        }
    }

    // 从 JSON 加载自定义字体
    private static void loadCustomFontsFromJson(ResourceManager manager) {
        forEachCustomResourceSection(h2 -> h2.subCategories, "ocelotsignmod.gui.tabs.fonts",
                section -> loadCustomFontsFromJsonSection(section, manager));
    }

    // 解析自定义字体 JSON 并追加到指定 Section
    private static void loadCustomFontsFromJsonSection(PatternAndFontOverlay.H4Section section, ResourceManager manager) {
        Identifier jsonId = Identifier.of(section.customJsonPath);

        try {
            List<Resource> resources = collectAllResources(manager, jsonId);
            for (Resource resource : resources) {
                try (InputStreamReader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
                    JsonElement root = JsonParser.parseReader(reader);
                    if (root.isJsonArray()) {
                        for (JsonElement element : root.getAsJsonArray()) {
                            JsonObject obj = element.getAsJsonObject();
                            String fontId = obj.has("font_id") ? obj.get("font_id").getAsString() : "";
                            String name = obj.has("name") ? obj.get("name").getAsString() : Text.translatable("ocelotsignmod.gui.unnamed_font").getString();

                            if (!fontId.isEmpty()) {
                                section.addFontItem(fontId, Text.literal(name));
                            }
                        }
                    }
                } catch (Exception innerE) {
                    System.err.println("[PatternRegistry] Failed to parse custom font JSON: " + innerE.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("[PatternRegistry] Custom font JSON 读取异常: " + e.getMessage());
        }
    }

    /**
     * 收集来自所有资源包的目标资源。
     * <p>遍历所有命名空间与路径匹配的 Identifier，使用 {@link ResourceManager#getAllResources(Identifier)}
     * 逐个收集同名资源，确保来自多个资源包的同名文件都被加载。
     *
     * @param manager 资源管理器
     * @param targetId 目标资源 ID
     * @return 所有匹配的 Resource 列表
     */
    private static List<Resource> collectAllResources(ResourceManager manager, Identifier targetId) {
        List<Resource> result = new ArrayList<>();
        try {
            Map<Identifier, Resource> allById = manager.findResources(
                    targetId.getPath(),
                    id -> id.getNamespace().equals(targetId.getNamespace())
                            && id.getPath().equals(targetId.getPath())
            );
            for (Identifier id : allById.keySet()) {
                try {
                    result.addAll(manager.getAllResources(id));
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        if (result.isEmpty()) {
            try {
                result.addAll(manager.getAllResources(targetId));
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    // 遍历自定义资源包 Section 并执行操作
    private static void forEachCustomResourceSection(java.util.function.Function<PatternAndFontOverlay.H2Category, java.util.List<PatternAndFontOverlay.H3Category>> childrenSupplier,
                                                     String tabTranslationKey,
                                                     java.util.function.Consumer<PatternAndFontOverlay.H4Section> sectionConsumer) {
        String customPackKey = "ocelotsignmod.gui.categories.custom_resource_pack";
        for (PatternAndFontOverlay.H2Category h2 : PatternAndFontOverlay.REGISTRY) {
            if (!h2.title.getString().equals(Text.translatable(tabTranslationKey).getString())) continue;
            for (PatternAndFontOverlay.H3Category h3 : childrenSupplier.apply(h2)) {
                if (!h3.title.getString().equals(Text.translatable(customPackKey).getString())) continue;
                for (PatternAndFontOverlay.H4Section section : h3.sections) {
                    if (section.customJsonPath.isEmpty()) continue;
                    sectionConsumer.accept(section);
                }
            }
        }
    }

    // 从 JSON 加载高级自定义 UI 定义
    private static void loadAdvancedCustomUIFromJson(ResourceManager manager) {
        Map<Identifier, Resource> resources = manager.findResources("ui_definitions",
                id -> id.getPath().endsWith(".json"));

        for (Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            try (InputStreamReader reader = new InputStreamReader(entry.getValue().getInputStream(), StandardCharsets.UTF_8)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

                String tabType = root.has("tab") ? root.get("tab").getAsString() : "patterns";
                boolean isFont = tabType.equals("fonts");

                PatternAndFontOverlay.H2Category targetH2 = isFont ? PatternAndFontOverlay.REGISTRY.get(1) : PatternAndFontOverlay.REGISTRY.get(0);
                PatternAndFontOverlay.H3Category customPackH3 = isFont ? targetH2.subCategories.get(1) : targetH2.subCategories.get(2);

                String categoryName = root.has("category_name") ? root.get("category_name").getAsString() : "未命名分类";
                PatternAndFontOverlay.H3Category customH3 = new PatternAndFontOverlay.H3Category(Text.literal(categoryName));

                customH3.headerText = root.has("header_text")
                        ? Text.literal(root.get("header_text").getAsString())
                        : Text.translatable(isFont
                                ? "ocelotsignmod.gui.sections.custom_fonts.desc"
                                : "ocelotsignmod.gui.sections.custom_patterns.desc");

                if (root.has("sections") && root.get("sections").isJsonArray()) {
                    JsonArray sectionsArray = root.getAsJsonArray("sections");
                    for (JsonElement secElement : sectionsArray) {
                        JsonObject secObj = secElement.getAsJsonObject();
                        PatternAndFontOverlay.H4Section newSection = parseSectionFromJson(secObj);
                        customH3.addSection(newSection);
                        buildTextureCache(newSection, manager);
                    }
                }

                customPackH3.addSubCategory(customH3);

            } catch (Exception e) {
                System.err.println("[PatternRegistry] 加载自定义 UI JSON 失败: " + entry.getKey() + " | 错误: " + e.getMessage());
            }
        }
    }

    // 从 JSON 解析 Section
    private static PatternAndFontOverlay.H4Section parseSectionFromJson(JsonObject secObj) {
        Text secTitle = Text.literal(secObj.has("title") ? secObj.get("title").getAsString() : "未命名 Section");
        Text secDesc = Text.literal(secObj.has("description") ? secObj.get("description").getAsString() : "");
        Identifier basePath = Identifier.of(secObj.has("basePath") ? secObj.get("basePath").getAsString() : "minecraft:empty/");

        PatternAndFontOverlay.H4Section newSection = new PatternAndFontOverlay.H4Section(secTitle, secDesc, basePath);

        if (secObj.has("useSubfolders") && secObj.get("useSubfolders").getAsBoolean()) {
            newSection.enableSubfolders();
            if (secObj.has("subFolders")) {
                for (JsonElement subEl : secObj.getAsJsonArray("subFolders")) {
                    JsonObject subObj = subEl.getAsJsonObject();
                    newSection.addSubFolder(subObj.get("dirName").getAsString(),
                            Text.literal(subObj.get("displayName").getAsString()));
                }
            }
        }

        if (secObj.has("filterMode")) {
            PatternAndFontOverlay.FilterMode mode = PatternAndFontOverlay.FilterMode.valueOf(secObj.get("filterMode").getAsString().toUpperCase());
            List<String> filters = new ArrayList<>();
            if (secObj.has("filterList")) {
                for (JsonElement filterEl : secObj.getAsJsonArray("filterList")) {
                    filters.add(filterEl.getAsString());
                }
            }
            newSection.setExtensionFilter(mode, filters.toArray(new String[0]));
        }

        if (secObj.has("isFontMode") && secObj.get("isFontMode").getAsBoolean()) {
            newSection.setFontMode();
            if (secObj.has("insertTemplate")) {
                newSection.setFontInsertTemplate(secObj.get("insertTemplate").getAsString());
            }

            if (secObj.has("fontList") && secObj.get("fontList").isJsonArray()) {
                for (JsonElement fontEl : secObj.getAsJsonArray("fontList")) {
                    JsonObject fontObj = fontEl.getAsJsonObject();
                    String fId = fontObj.get("fontId").getAsString();
                    String dName = fontObj.get("displayName").getAsString();
                    newSection.addFontItem(fId, Text.literal(dName));
                }
            }
        }

        return newSection;
    }

    /**
     * 注册资源包刷新监听器，确保资源包刷新后自动同步缓存。
     */
    public static void registerReloadListener() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(
                new SimpleSynchronousResourceReloadListener() {
                    @Override
                    public Identifier getFabricId() {
                        return OcelotSignMod.id("pattern_registry_reloader");
                    }

                    @Override
                    public void reload(ResourceManager manager) {
                        onResourcePackReloaded(manager);
                    }
                }
        );
    }

    // 资源包刷新回调
    private static void onResourcePackReloaded(ResourceManager manager) {
        // 重置 PatternAndFontOverlay 的所有数据（清空 REGISTRY 和 MISHANG_PATTERNS）
        PatternAndFontOverlay.resetForReload();

        // 重新构建注册表
        registerBuiltInPatterns();

        OcelotSignMod.LOGGER.info("[PatternRegistry] 资源包刷新完成，注册表已重建");
    }

    // 递归清空 H3 节点下所有缓存
    private static void clearTextureCacheRecursive(PatternAndFontOverlay.H3Category h3) {
        for (PatternAndFontOverlay.H4Section section : h3.sections) {
            section.cachedTextures.clear();
            section.whitelistItems.clear();
            section.fontItems.clear();
        }
        for (PatternAndFontOverlay.H3Category child : h3.subCategories) {
            clearTextureCacheRecursive(child);
        }
    }

    // 递归重建 H3 节点下所有纹理缓存
    private static void rebuildTextureCacheRecursive(PatternAndFontOverlay.H3Category h3, ResourceManager manager) {
        for (PatternAndFontOverlay.H4Section section : h3.sections) {
            buildTextureCache(section, manager);
        }
        for (PatternAndFontOverlay.H3Category child : h3.subCategories) {
            rebuildTextureCacheRecursive(child, manager);
        }
    }

    // 递归寻找首个叶子 H3 节点
    private static PatternAndFontOverlay.H3Category findFirstLeafH3(PatternAndFontOverlay.H3Category h3) {
        if (h3.subCategories.isEmpty()) {
            return h3;
        }
        return findFirstLeafH3(h3.subCategories.get(0));
    }
}
