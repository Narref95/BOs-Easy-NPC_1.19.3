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

package de.markusbordihn.easynpc.entity;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import de.markusbordihn.easynpc.action.ActionType;
import de.markusbordihn.easynpc.action.ActionUtils;
import de.markusbordihn.easynpc.commands.CommandManager;

public class EasyNPCEntity extends EasyNPCEntityData {

  // Shared constants
  public static final MobCategory CATEGORY = MobCategory.MISC;

  public EasyNPCEntity(EntityType<? extends EasyNPCEntity> entityType, Level level,
      Enum<?> variant) {
    this(entityType, level);
    this.setVariant(variant);
  }

  public EasyNPCEntity(EntityType<? extends EasyNPCEntity> entityType, Level level) {
    super(entityType, level);
    this.setInvulnerable(true);
  }

  public void finalizeSpawn() {
    // Do stuff like default names.
  }

  public void executeAction(ActionType actionType, ServerPlayer serverPlayer) {
    if (!this.hasAction(actionType)) {
      return;
    }
    String action = ActionUtils.parseAction(this.getAction(actionType), this, serverPlayer);
    boolean debug = this.getActionDebug();
    int permissionLevel = this.getActionPermissionLevel();
    log.debug("Execute action {}:{} for {} with permission level {} ...", actionType, action, this,
        permissionLevel);
    CommandManager.executeEntityCommand(action, this, permissionLevel, debug);
  }

  @Override
  public boolean isAttackable() {
    return false;
  }

  @Override
  public boolean isPushable() {
    return false;
  }

  @Override
  public boolean removeWhenFarAway(double distance) {
    return false;
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
    this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 15.0F));
  }

  @Override
  public void travel(Vec3 vec3) {
    // Make sure we only calculate animations for be as much as possible server-friendly.
    this.calculateEntityAnimation(this, this instanceof FlyingAnimal);
  }

  @Override
  @Nullable
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor,
      DifficultyInstance difficulty, MobSpawnType mobSpawnType,
      @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
    spawnGroupData = super.finalizeSpawn(serverLevelAccessor, difficulty, mobSpawnType,
        spawnGroupData, compoundTag);

    finalizeSpawn();
    return spawnGroupData;
  }

  @Override
  public InteractionResult mobInteract(Player player, InteractionHand hand) {
    boolean isClientSide = this.level.isClientSide;
    log.debug("mobInteract: {} {} {} {}", this.getUUID(), player, hand, isClientSide);

    if (player instanceof ServerPlayer serverPlayer && hand == InteractionHand.MAIN_HAND) {
      boolean hasInteractionAction = this.hasAction(ActionType.ON_INTERACTION);
      if (player.isCreative()
          && ((!this.hasDialog() && !hasInteractionAction) || player.isCrouching())) {
        EasyNPCEntityMenu.openMainConfigurationMenu(serverPlayer, this);
        return InteractionResult.PASS;
      }

      if (hasInteractionAction) {
        this.executeAction(ActionType.ON_INTERACTION, serverPlayer);
      }

      if (this.hasDialog()) {
        EasyNPCEntityMenu.openDialogMenu(serverPlayer, this);
      }
    }

    return InteractionResult.PASS;
  }

}
