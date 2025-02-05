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

package de.markusbordihn.easynpc.menu.configuration;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;

import de.markusbordihn.easynpc.Constants;
import de.markusbordihn.easynpc.entity.EasyNPCEntity;
import de.markusbordihn.easynpc.entity.EntityManager;
import de.markusbordihn.easynpc.skin.SkinModel;

public class ConfigurationMenu extends AbstractContainerMenu {

  protected static final Logger log = LogManager.getLogger(Constants.LOG_NAME);

  // Cache
  protected final EasyNPCEntity entity;
  protected final Level level;
  protected final Player player;
  protected final SkinModel skinModel;
  protected final UUID uuid;

  public ConfigurationMenu(final MenuType<?> menuType, final int windowId,
      final Inventory playerInventory, UUID uuid) {
    super(menuType, windowId);

    this.uuid = uuid;
    this.player = playerInventory.player;
    this.level = player.getLevel();
    this.entity = this.level.isClientSide ? EntityManager.getEasyNPCEntityByUUID(uuid)
        : EntityManager.getEasyNPCEntityByUUID(uuid, (ServerPlayer) player);
    this.skinModel = this.entity.getSkinModel();

    log.debug("Open configuration menu for {}: {} with player inventory {}", this.uuid, this.entity,
        playerInventory);
  }

  public EasyNPCEntity getEntity() {
    return this.entity;
  }

  @Override
  public boolean stillValid(Player player) {
    return player != null && player.isAlive() && entity != null && entity.isAlive();
  }

}
