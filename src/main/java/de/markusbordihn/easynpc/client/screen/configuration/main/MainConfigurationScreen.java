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

package de.markusbordihn.easynpc.client.screen.configuration.main;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import de.markusbordihn.easynpc.Constants;
import de.markusbordihn.easynpc.client.screen.ScreenHelper;
import de.markusbordihn.easynpc.client.screen.configuration.ConfigurationScreen;
import de.markusbordihn.easynpc.dialog.DialogType;
import de.markusbordihn.easynpc.menu.configuration.ConfigurationType;
import de.markusbordihn.easynpc.menu.configuration.main.MainConfigurationMenu;
import de.markusbordihn.easynpc.network.NetworkHandler;
import de.markusbordihn.easynpc.skin.SkinType;

@OnlyIn(Dist.CLIENT)
public class MainConfigurationScreen extends ConfigurationScreen<MainConfigurationMenu> {

  // Buttons and boxes
  protected Button editActionButton = null;
  protected Button editDialogButton = null;
  protected Button editEquipmentButton = null;
  protected Button editSkinButton = null;
  protected Button removeEntityButton = null;
  protected Button saveNameButton = null;
  protected Button scalingButton = null;
  private EditBox nameBox;

  // Cache
  private String formerName = "";

  public MainConfigurationScreen(MainConfigurationMenu menu, Inventory inventory,
      Component component) {
    super(menu, inventory, component);
  }

  private void saveName() {
    String value = this.nameBox.getValue();
    if (value != null && !value.isBlank()) {
      log.debug("Saving name {} for {}", value, this.entity);
      NetworkHandler.nameChange(this.uuid, value);
      this.formerName = value;
      this.saveNameButton.active = false;
    }
  }

  public void deleteNPC() {
    Minecraft minecraft = this.minecraft;
    if (minecraft == null) {
      return;
    }
    minecraft.setScreen(new ConfirmScreen(confirmed -> {
      if (confirmed && uuid != null) {
        NetworkHandler.removeNPC(uuid);
        minecraft.setScreen((Screen) null);
      } else {
        minecraft.setScreen(this);
      }
    }, new TranslatableComponent(Constants.TEXT_PREFIX + "removeNPC.deleteQuestion"),
        new TranslatableComponent(Constants.TEXT_PREFIX + "removeNPC.deleteWarning",
            this.entity.getDisplayName().getString()),
        new TranslatableComponent(Constants.TEXT_PREFIX + "removeNPC.deleteButton"),
        CommonComponents.GUI_CANCEL));
  }

  private void validateName() {
    String nameValue = this.nameBox.getValue();
    this.saveNameButton.active = nameValue != null && !this.formerName.equals(nameValue);
  }

  @Override
  public void init() {
    if (this.entity == null) {
      return;
    }
    super.init();

    // Button positions
    int buttonLeftPosition = this.leftPos + 110;
    int buttonSpace = 4;
    int buttonTopPosition = this.topPos + 54;
    int buttonWidth = 88;
    int buttonHeight = 20;

    // Hide home button
    this.homeButton.visible = false;

    // Name Edit Box and Save Button
    this.formerName = this.entity.getName().getString();
    this.nameBox = new EditBox(this.font, this.contentLeftPos + 1, this.topPos + 31, 190, 18,
        new TranslatableComponent("Name"));
    this.nameBox.setMaxLength(32);
    this.nameBox.setValue(this.formerName);
    this.nameBox.setResponder(consumer -> this.validateName());
    this.addRenderableWidget(this.nameBox);

    this.saveNameButton =
        this.addRenderableWidget(new Button(this.leftPos + 202, this.topPos + 30, buttonWidth, 20,
            new TranslatableComponent(Constants.TEXT_CONFIG_PREFIX + "save_name"), onPress -> {
              this.saveName();
            }));
    this.saveNameButton.active = false;

    // Skins Button
    this.editSkinButton =
        this.addRenderableWidget(new Button(this.contentLeftPos, this.topPos + 205, 100, 20,
            new TranslatableComponent(Constants.TEXT_CONFIG_PREFIX + "skin"), onPress -> {
              SkinType skinType = this.entity.getSkinType();
              switch (skinType) {
                case PLAYER_SKIN:
                case SECURE_REMOTE_URL:
                case INSECURE_REMOTE_URL:
                  NetworkHandler.openConfiguration(uuid, ConfigurationType.PLAYER_SKIN);
                  break;
                case CUSTOM:
                  NetworkHandler.openConfiguration(uuid, ConfigurationType.CUSTOM_SKIN);
                  break;
                default:
                  NetworkHandler.openConfiguration(uuid, ConfigurationType.DEFAULT_SKIN);
              }
            }));

    // Dialog Button
    this.editDialogButton = this.addRenderableWidget(
        new Button(buttonLeftPosition, buttonTopPosition, buttonWidth, buttonHeight,
            new TranslatableComponent(Constants.TEXT_CONFIG_PREFIX + "dialog"), onPress -> {
              DialogType dialogType = this.entity.getDialogType();
              switch (dialogType) {
                case BASIC:
                  NetworkHandler.openConfiguration(uuid, ConfigurationType.BASIC_DIALOG);
                  break;
                case YES_NO:
                  NetworkHandler.openConfiguration(uuid, ConfigurationType.YES_NO_DIALOG);
                  break;
                default:
                  NetworkHandler.openConfiguration(uuid, ConfigurationType.BASIC_DIALOG);
              }
            }));

    // Actions Button
    this.editActionButton = this.addRenderableWidget(
        new Button(this.editDialogButton.x + this.editDialogButton.getWidth() + buttonSpace,
            buttonTopPosition, buttonWidth, buttonHeight,
            new TranslatableComponent(Constants.TEXT_CONFIG_PREFIX + "actions"), onPress -> {
              NetworkHandler.openConfiguration(uuid, ConfigurationType.BASIC_ACTION);
            }));

    buttonTopPosition = buttonTopPosition + buttonHeight + buttonSpace;

    // Equipment Button
    this.editEquipmentButton = this.addRenderableWidget(
        new Button(buttonLeftPosition, buttonTopPosition, buttonWidth, buttonHeight,
            new TranslatableComponent(Constants.TEXT_CONFIG_PREFIX + "equipment"), onPress -> {
              NetworkHandler.openConfiguration(uuid, ConfigurationType.EQUIPMENT);
            }));

    // Scaling Button
    this.scalingButton = this.addRenderableWidget(
        new Button(this.editEquipmentButton.x + this.editEquipmentButton.getWidth() + buttonSpace,
            buttonTopPosition, buttonWidth, buttonHeight,
            new TranslatableComponent(Constants.TEXT_CONFIG_PREFIX + "scaling"), onPress -> {
              NetworkHandler.openConfiguration(uuid, ConfigurationType.SCALING);
            }));

    // Delete Button
    this.removeEntityButton =
        this.addRenderableWidget(new Button(this.rightPos - 60, this.bottomPos - 30, 50, 20,
            new TranslatableComponent(Constants.TEXT_CONFIG_PREFIX + "delete")
                .withStyle(ChatFormatting.RED),
            onPress -> {
              deleteNPC();
            }));
  }

  @Override
  public void render(PoseStack poseStack, int x, int y, float partialTicks) {
    this.renderBackground(poseStack);
    super.render(poseStack, x, y, partialTicks);
    this.xMouse = x;
    this.yMouse = y;

    // Avatar
    ScreenHelper.renderScaledEntityAvatar(this.leftPos + 55, this.topPos + 195, 55,
        this.leftPos + 50 - this.xMouse, this.topPos + 90 - this.yMouse, this.entity);

    // Entity Type
    this.font.draw(poseStack, entity.getType().getDescription(), this.contentLeftPos + 2f,
        this.topPos + 58f, 4210752);
  }

  @Override
  protected void renderLabels(PoseStack poseStack, int x, int y) {
    this.font.draw(poseStack, this.title, this.titleLabelX, this.titleLabelY, 4210752);
  }

  @Override
  protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
    super.renderBg(poseStack, partialTicks, mouseX, mouseY);

    // Entity Type
    fill(poseStack, this.contentLeftPos, this.topPos + 54, this.leftPos + 107, this.topPos + 191,
        0xff000000);
    fill(poseStack, this.leftPos + 8, this.topPos + 55, this.leftPos + 106, this.topPos + 190,
        0xffffffff);

    // Entity
    fill(poseStack, this.contentLeftPos, this.topPos + 69, this.leftPos + 107, this.topPos + 206,
        0xff000000);
    fill(poseStack, this.leftPos + 8, this.topPos + 70, this.leftPos + 106, this.topPos + 205,
        0xffaaaaaa);
  }

}
