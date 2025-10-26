package com.sorrowmist.useless.items;
import com.sorrowmist.useless.UselessMod;
import com.sorrowmist.useless.blocks.GlowPlasticBlock;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;


public class EndlessBeafItem extends PickaxeItem {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UselessMod.MOD_ID);



    public EndlessBeafItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }



    public static void init(IEventBus iEventBus){
        ITEMS.register(iEventBus);
    }


    @Override
    public boolean isDamageable(ItemStack stack) {
        return false; // 物品不可损坏
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 0; // 最大耐久为0，表示无限
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return false; // 不显示耐久条
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        // 阻止任何耐久度设置
        super.setDamage(stack, 0);
    }





    // 检查是否处于精准采集模式
    public boolean isSilkTouchMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.getBoolean("SilkTouchMode");
    }

    // 更新实际的附魔NBT
    // 更新实际的附魔NBT
    public void updateEnchantments(ItemStack stack) {
        // 获取现有的所有附魔
        Map<Enchantment, Integer> enchantments = new HashMap<>(EnchantmentHelper.getEnchantments(stack));
        CompoundTag tag = stack.getOrCreateTag();
        enchantments.put(Enchantments.MOB_LOOTING, 10);

        if (isSilkTouchMode(stack)) {
            // 精准采集模式 → 移除时运，确保有精准采集

            // 1. 保存外部时运等级（如果有）
            int currentFortune = enchantments.getOrDefault(Enchantments.BLOCK_FORTUNE, 0);
            if (currentFortune > 10) { // 只保存超过默认10级的附魔
                tag.putInt("SavedFortuneLevel", currentFortune);
            }

            // 2. 移除时运附魔
            enchantments.remove(Enchantments.BLOCK_FORTUNE);

            // 3. 确保有精准采集附魔
            int currentSilkTouch = enchantments.getOrDefault(Enchantments.SILK_TOUCH, 0);
            int savedSilkTouch = tag.getInt("SavedSilkTouchLevel");
            int finalSilkTouchLevel = Math.max(1, Math.max(currentSilkTouch, savedSilkTouch));

            enchantments.put(Enchantments.SILK_TOUCH, finalSilkTouchLevel);

        } else {
            // 时运模式 → 移除精准采集，确保有时运

            // 1. 保存外部精准采集等级（如果有）
            int currentSilkTouch = enchantments.getOrDefault(Enchantments.SILK_TOUCH, 0);
            if (currentSilkTouch > 1) { // 只保存超过默认1级的附魔
                tag.putInt("SavedSilkTouchLevel", currentSilkTouch);
            }

            // 2. 移除精准采集附魔
            enchantments.remove(Enchantments.SILK_TOUCH);

            // 3. 确保有时运附魔
            int currentFortune = enchantments.getOrDefault(Enchantments.BLOCK_FORTUNE, 0);
            int savedFortune = tag.getInt("SavedFortuneLevel");
            int finalFortuneLevel = Math.max(10, Math.max(currentFortune, savedFortune));

            enchantments.put(Enchantments.BLOCK_FORTUNE, finalFortuneLevel);
        }

        // 应用更新后的附魔
        EnchantmentHelper.setEnchantments(enchantments, stack);
    }

    // 切换模式的方法（供数据包调用）
    public void switchEnchantmentMode(ItemStack stack, boolean silkTouchMode) {
        stack.getOrCreateTag().putBoolean("SilkTouchMode", silkTouchMode);
        updateEnchantments(stack);
    }



    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        // 显示当前模式
        if (isSilkTouchMode(stack)) {
            tooltip.add(Component.translatable("tooltip.useless_mod.silk_touch_mode").withStyle(ChatFormatting.AQUA));
            int silkTouchLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
            if (silkTouchLevel > 0) {
                tooltip.add(Component.translatable("tooltip.useless_mod.silk_touch_level", silkTouchLevel).withStyle(ChatFormatting.GOLD));
            }
        } else {
            tooltip.add(Component.translatable("tooltip.useless_mod.fortune_mode").withStyle(ChatFormatting.GOLD));
            int fortuneLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack);
            if (fortuneLevel > 0) {
                tooltip.add(Component.translatable("tooltip.useless_mod.fortune_level", fortuneLevel).withStyle(ChatFormatting.GREEN));
                if (fortuneLevel > 10) {
                    tooltip.add(Component.translatable("tooltip.useless_mod.external_enchantment").withStyle(ChatFormatting.RED));
                }
            }
        }

        // 功能提示
        tooltip.add(Component.translatable("tooltip.useless_mod.switch_enchantment").withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltip.add(Component.translatable("tooltip.useless_mod.wrench_function").withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.translatable("tooltip.useless_mod.fast_break_plastic").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.translatable("tooltip.useless_mod.festive_affix").withStyle(ChatFormatting.BLUE));
    }



    @Override
    public boolean isFoil(ItemStack stack) {
        return true; // 始终显示附魔光效
    }

    // 禁止效率附魔
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.BLOCK_EFFICIENCY) {
            return false; // 禁止效率附魔
        }
        return true; // 允许其他所有附魔
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 30; // 允许被附魔
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true; // 允许被附魔
    }

    // 重写获取附魔等级的方法 - 现在直接使用NBT中的附魔数据
    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        return EnchantmentHelper.getTagEnchantmentLevel(enchantment, stack);
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level level, Player player) {
        super.onCraftedBy(stack, level, player);
        // 首次创建时设置为时运模式
        if (!stack.hasTag() || !stack.getTag().contains("SilkTouchMode")) {
            switchEnchantmentMode(stack, false); // 默认时运模式
        }else {
            // 确保已有抢夺附魔
            updateEnchantments(stack);
        }
    }



    public static final RegistryObject<Item> ENDLESS_BEAF_ITEM = ITEMS.register("endless_beaf_item",
            () -> new EndlessBeafItem(
                    Tiers.NETHERITE,  // Tier - 可根据需要调整
                    50,               // Attack damage modifier
                    2.0f,            // Attack speed modifier
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.EPIC)
                            .durability(0)
            ) {


                @Override
                public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
                    return state.is(BlockTags.MINEABLE_WITH_PICKAXE) ||
                            state.is(BlockTags.MINEABLE_WITH_AXE) ||
                            state.is(BlockTags.MINEABLE_WITH_SHOVEL) ||
                            state.is(BlockTags.MINEABLE_WITH_HOE);
                }
            });
    // 检查是否触发战利品大爆发
    private boolean shouldTriggerFestive(ItemStack stack) {
        // 20% 概率
        return Math.random() < 0.05;
    }

    // 显示触发提示
    private void sendFestiveMessage(Player player) {
        if (player != null) {
            player.displayClientMessage(
                    Component.translatable("message.useless_mod.festive_triggered"),
                    true
            );
        }
    }

    @Mod.EventBusSubscriber(modid = UselessMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventHandler {
        @SubscribeEvent
        public static void onLivingDrops(LivingDropsEvent event) {
            // 检查伤害来源是否是玩家
            if (event.getSource().getEntity() instanceof Player player) {
                ItemStack mainHandItem = player.getMainHandItem();

                // 检查主手物品是否是EndlessBeafItem
                if (mainHandItem.getItem() instanceof EndlessBeafItem endlessBeaf) {
                    endlessBeaf.onLivingDrops(event, mainHandItem, player);
                }
            }
        }
    }

    // 处理掉落物事件的方法
    public void onLivingDrops(LivingDropsEvent event, ItemStack stack, Player player) {
        if (!shouldTriggerFestive(stack)) {
            return;
        }

        LivingEntity killedEntity = event.getEntity();
        Level level = killedEntity.level();

        if (!level.isClientSide) {


            // 显示提示消息
            sendFestiveMessage(player);
            // 直接修改掉落物堆叠数量 - 更简单有效的方法
            Collection<ItemEntity> drops = event.getDrops();
            List<ItemEntity> newDrops = new ArrayList<>();

            for (ItemEntity itemEntity : drops) {
                if (!isEquipment(itemEntity.getItem())) {
                    ItemStack itemStack = itemEntity.getItem();
                    // 直接将堆叠数量乘以20
                    int originalCount = itemStack.getCount();
                    itemStack.setCount(originalCount * 20);

                    // 重新创建ItemEntity以确保更新
                    ItemEntity newItem = new ItemEntity(
                            level,
                            itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
                            itemStack
                    );
                    newItem.setDeltaMovement(
                            -0.3 + level.random.nextDouble() * 0.6,
                            0.3 + level.random.nextDouble() * 0.3,
                            -0.3 + level.random.nextDouble() * 0.6
                    );
                    newDrops.add(newItem);
                } else {
                    newDrops.add(itemEntity);
                }
            }

            // 清空原掉落物列表并添加新的
            drops.clear();
            drops.addAll(newDrops);
        }
    }

    // 检查物品是否是装备（基于Festive Affix的逻辑）
    private boolean isEquipment(ItemStack stack) {
        // 检查是否有装备标记（基于Festive Affix的逻辑）
        if (stack.hasTag() && stack.getTag().getBoolean("apoth.equipment")) {
            return true;
        }

        // 可损坏的物品通常是装备（工具、武器、盔甲）
        return stack.isDamageableItem();
    }


    @Override
    public Component getName(ItemStack stack) {
        // 获取基础名称
        Component baseName = super.getName(stack);

        // 根据模式添加后缀
        if (isSilkTouchMode(stack)) {
            return Component.translatable("item.useless_mod.endless_beaf_item.silk_touch");
        } else {
            return Component.translatable("item.useless_mod.endless_beaf_item.fortune");
        }
    }


    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if(pEntity instanceof Player player){
            boolean hasItemInInventory = player.getInventory().items.stream()
                    .anyMatch(item -> item.getItem() == this);

            if (hasItemInInventory) {
                // 给予饱和效果（不显示粒子，但显示图标）
                MobEffectInstance baohe = player.getEffect(MobEffects.SATURATION);
                if (baohe == null || baohe.getDuration() < 20) {
                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 200, 0, true, false, true));
                }

                // 给予生命恢复效果（不显示粒子，但显示图标）
                MobEffectInstance zaisheng = player.getEffect(MobEffects.REGENERATION);
                if (zaisheng == null || (zaisheng.getDuration() < 20)) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 5, true, false, true));
                }

                // 给予夜视效果（不显示粒子，但显示图标）
                MobEffectInstance yeshi = player.getEffect(MobEffects.NIGHT_VISION);
                if (yeshi == null || (yeshi.getDuration() < 2000)) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20000, 0, true, false, true));
                }
            }
        }
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull BlockState state) {
        return true;
    }


    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        // 声明这个物品可以执行斧头和锄头的所有动作
        return toolAction.equals(ToolActions.AXE_STRIP) ||
                toolAction.equals(ToolActions.AXE_SCRAPE) ||
                toolAction.equals(ToolActions.AXE_WAX_OFF) ||
                net.minecraftforge.common.ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction) ||
                super.canPerformAction(stack, toolAction)||
                toolAction.equals(ToolActions.HOE_TILL);

    }

    private boolean isPlasticBlock(Block block) {
        // 直接检查是否是 GlowPlasticBlock 的实例
        if (block instanceof GlowPlasticBlock) {
            return true;
        }
        return false;
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState blockstate = world.getBlockState(blockpos);
        BlockState resultToSet = null;



        if (player != null && player.isShiftKeyDown()) {
            // 检查目标方块是否是你的塑料方块
            if (isPlasticBlock(blockstate.getBlock())) {
                if (!world.isClientSide) {
                    // 服务器端：手动处理方块破坏，不显示粒子效果
                    Block.dropResources(blockstate, world, blockpos, null, player, context.getItemInHand());
                    world.setBlock(blockpos, Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
                    world.playSound(player, blockpos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                } else {
                    // 客户端：播放声音
                    world.playSound(player, blockpos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                return InteractionResult.sidedSuccess(world.isClientSide);
            }
        }

        // 1. 首先尝试作为斧头使用（去皮）
        BlockState axeResult = blockstate.getToolModifiedState(context, ToolActions.AXE_STRIP, false);
        if (axeResult != null) {
            world.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            resultToSet = axeResult;
        }

        // 2. 尝试作为斧头刮蜡
        if (resultToSet == null) {
            BlockState scrapeResult = blockstate.getToolModifiedState(context, ToolActions.AXE_SCRAPE, false);
            if (scrapeResult != null) {
                world.playSound(player, blockpos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                resultToSet = scrapeResult;
            }
        }

        // 3. 尝试作为斧头氧化
        if (resultToSet == null) {
            BlockState oxidizeResult = blockstate.getToolModifiedState(context, ToolActions.AXE_WAX_OFF, false);
            if (oxidizeResult != null) {
                world.playSound(player, blockpos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                resultToSet = oxidizeResult;
            }
        }

        // 4. 最后尝试作为锄头使用（耕地）
        if (resultToSet == null) {
            BlockState hoeResult = blockstate.getToolModifiedState(context, ToolActions.HOE_TILL, false);
            if (hoeResult != null) {
                world.playSound(player, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
                resultToSet = hoeResult;
            }
        }

        if (resultToSet == null) {
            return InteractionResult.PASS;
        }

        if (!world.isClientSide) {
            ItemStack stack = context.getItemInHand();
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, blockpos, stack);
            }
            world.setBlock(blockpos, resultToSet, Block.UPDATE_ALL_IMMEDIATE);
            if (player != null) {
                stack.hurtAndBreak(1, player, onBroken -> onBroken.broadcastBreakEvent(context.getHand()));
            }
        }

        return InteractionResult.sidedSuccess(world.isClientSide);
    }




    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        // 获取基础破坏速度
        float baseSpeed = 20.0f;

        // 只对有效方块应用速度加成
        if (state.getDestroySpeed(null, null) > 0) {
            // 应用类似MinersFervorEnchant的机制
            // 基础速度7.5F + 每级4.5F加成，最大29.9999F
            float maxSpeed = Math.min(29.9999F, baseSpeed);
            float hardness = state.getDestroySpeed(null, null);
            if (hardness > 0) {
                return maxSpeed * hardness;
            }
        }

        return baseSpeed;
    }
}

