package com.sorrowmist.useless.items;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sorrowmist.useless.UselessMod;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class EndlessBeafItem extends  Item{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, UselessMod.MOD_ID);
    UUID uuidattack = UUID.randomUUID();
    UUID uuidluck = UUID.randomUUID();
    //UUID uuidhealth = UUID.randomUUID();
    //private static final int BREAK_COOLDOWN = 5;
    private static final Map<UUID, Long> lastBreakTimes = new HashMap<>();



    public EndlessBeafItem(Properties pProperties) {
        super(pProperties);
    }

    public static void init(IEventBus iEventBus){
        ITEMS.register(iEventBus);
    }



    public static final RegistryObject<Item> ENDLESS_BEAF_ITEM = ITEMS.register("endless_beaf_item",
            () -> new EndlessBeafItem(new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.EPIC)
                    .food(new FoodProperties.Builder()
                            .nutrition(200)
                            .saturationMod(20)
                            .build())
                    .durability(0)
            ));

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> ChangeAttribute = HashMultimap.create();
        if(slot == EquipmentSlot.MAINHAND) {
            ChangeAttribute.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(uuidattack, "Attack Damage", 50, AttributeModifier.Operation.ADDITION));
            ChangeAttribute.put(Attributes.LUCK,
                    new AttributeModifier(uuidluck, "Luck", 50, AttributeModifier.Operation.ADDITION));
        }
        return ChangeAttribute;
    }






    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if(pEntity instanceof Player player){
            boolean hasItemInInventory = player.getInventory().items.stream()
                    .anyMatch(item -> item.getItem() == this);

            if (hasItemInInventory) {
                MobEffectInstance baohe = player.getEffect(MobEffects.SATURATION);
                if (baohe == null || baohe.getDuration() < 20) {
                    player.addEffect(new MobEffectInstance(MobEffects.SATURATION, 200, 0));
                }
                MobEffectInstance zaisheng =player.getEffect(MobEffects.REGENERATION);
                if (zaisheng == null || (zaisheng.getDuration() < 20)) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 5));
                }
                //添加夜视效果
                MobEffectInstance yeshi = player.getEffect(MobEffects.NIGHT_VISION);
                if (yeshi == null || (yeshi.getDuration() < 2000)) {
                    player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 20000, 0));
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
                toolAction.equals(ToolActions.HOE_TILL);
    }
    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState blockstate = world.getBlockState(blockpos);
        BlockState resultToSet = null;

        if (context.getClickedFace() == Direction.DOWN) {
            return InteractionResult.PASS;
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

