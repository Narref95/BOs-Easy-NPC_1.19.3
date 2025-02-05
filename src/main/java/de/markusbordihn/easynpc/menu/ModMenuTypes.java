/**
 * Copyright 2023 Markus Bordihn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.markusbordihn.easynpc.menu;

import net.minecraft.world.inventory.MenuType;

import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import de.markusbordihn.easynpc.Constants;
import de.markusbordihn.easynpc.menu.configuration.action.BasicActionConfigurationMenu;
import de.markusbordihn.easynpc.menu.configuration.dialog.BasicDialogConfigurationMenu;
import de.markusbordihn.easynpc.menu.configuration.dialog.YesNoDialogConfigurationMenu;
import de.markusbordihn.easynpc.menu.configuration.equipment.EquipmentConfigurationMenu;
import de.markusbordihn.easynpc.menu.configuration.main.MainConfigurationMenu;
import de.markusbordihn.easynpc.menu.configuration.scaling.ScalingConfigurationMenu;
import de.markusbordihn.easynpc.menu.configuration.skin.CustomSkinConfigurationMenu;
import de.markusbordihn.easynpc.menu.configuration.skin.DefaultSkinConfigurationMenu;
import de.markusbordihn.easynpc.menu.configuration.skin.PlayerSkinConfigurationMenu;

public class ModMenuTypes {

  protected ModMenuTypes() {

  }

  public static final DeferredRegister<MenuType<?>> MENU_TYPES =
      DeferredRegister.create(ForgeRegistries.CONTAINERS, Constants.MOD_ID);

  // Dialog
  public static final RegistryObject<MenuType<DialogMenu>> DIALOG_MENU =
      MENU_TYPES.register("dialog_menu", () -> IForgeMenuType.create(DialogMenu::new));

  // Configuration
  public static final RegistryObject<MenuType<MainConfigurationMenu>> MAIN_CONFIGURATION_MENU =
      MENU_TYPES.register("main_configuration_menu",
          () -> IForgeMenuType.create(MainConfigurationMenu::new));

  // Action Configuration
  public static final RegistryObject<MenuType<BasicActionConfigurationMenu>> BASIC_ACTION_CONFIGURATION_MENU =
      MENU_TYPES.register("basic_action_configuration_menu",
          () -> IForgeMenuType.create(BasicActionConfigurationMenu::new));

  // Dialog Configuration
  public static final RegistryObject<MenuType<BasicDialogConfigurationMenu>> BASIC_DIALOG_CONFIGURATION_MENU =
      MENU_TYPES.register("basic_dialog_configuration_menu",
          () -> IForgeMenuType.create(BasicDialogConfigurationMenu::new));
  public static final RegistryObject<MenuType<YesNoDialogConfigurationMenu>> YES_NO_DIALOG_CONFIGURATION_MENU =
      MENU_TYPES.register("yes_no_dialog_configuration_menu",
          () -> IForgeMenuType.create(YesNoDialogConfigurationMenu::new));

  // Equipment Configuration
  public static final RegistryObject<MenuType<EquipmentConfigurationMenu>> EQUIPMENT_CONFIGURATION_MENU =
      MENU_TYPES.register("equipment_configuration_menu",
          () -> IForgeMenuType.create(EquipmentConfigurationMenu::new));

  // Skin Configuration
  public static final RegistryObject<MenuType<CustomSkinConfigurationMenu>> CUSTOM_SKIN_CONFIGURATION_MENU =
      MENU_TYPES.register("custom_skin_configuration_menu",
          () -> IForgeMenuType.create(CustomSkinConfigurationMenu::new));
  public static final RegistryObject<MenuType<DefaultSkinConfigurationMenu>> DEFAULT_SKIN_CONFIGURATION_MENU =
      MENU_TYPES.register("default_skin_configuration_menu",
          () -> IForgeMenuType.create(DefaultSkinConfigurationMenu::new));
  public static final RegistryObject<MenuType<PlayerSkinConfigurationMenu>> PLAYER_SKIN_CONFIGURATION_MENU =
      MENU_TYPES.register("player_skin_configuration_menu",
          () -> IForgeMenuType.create(PlayerSkinConfigurationMenu::new));

  // Scaling Configuration
  public static final RegistryObject<MenuType<ScalingConfigurationMenu>> SCALING_CONFIGURATION_MENU =
      MENU_TYPES.register("scaling_configuration_menu",
          () -> IForgeMenuType.create(ScalingConfigurationMenu::new));
}
