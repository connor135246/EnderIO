package crazypants.enderio.base.filter.fluid.items;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.enderio.core.api.client.gui.IResourceTooltipProvider;
import com.enderio.core.client.handlers.SpecialTooltipHandler;

import crazypants.enderio.base.EnderIOTab;
import crazypants.enderio.base.filter.FilterRegistry;
import crazypants.enderio.base.filter.IItemFilterUpgrade;
import crazypants.enderio.base.filter.fluid.FluidFilter;
import crazypants.enderio.base.filter.fluid.IFluidFilter;
import crazypants.enderio.base.init.IModObject;
import crazypants.enderio.base.lang.Lang;
import crazypants.enderio.util.NbtValue;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFluidFilter extends Item implements IItemFilterUpgrade<IFluidFilter>, IResourceTooltipProvider {

  public static ItemFluidFilter create(@Nonnull IModObject modObject) {
    return new ItemFluidFilter(modObject);
  }

  protected ItemFluidFilter(@Nonnull IModObject modObject) {
    setCreativeTab(EnderIOTab.tabEnderIOItems);
    modObject.apply(this);
    setHasSubtypes(true);
    setMaxDamage(0);
    setMaxStackSize(64);
  }

  @Override
  public IFluidFilter createFilterFromStack(@Nonnull ItemStack stack) {
    IFluidFilter filter = new FluidFilter();
    if (NbtValue.FILTER.hasTag(stack)) {
      filter.readFromNBT(NbtValue.FILTER.getTag(stack));
    }
    return filter;
  }

  @Override
  public @Nonnull String getUnlocalizedName(@Nonnull ItemStack stack) {
    return getUnlocalizedName();
  }

  @Override
  public @Nonnull String getUnlocalizedNameForTooltip(@Nonnull ItemStack stack) {
    return getUnlocalizedName();
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);
    if (FilterRegistry.isFilterSet(stack)) {
      if (SpecialTooltipHandler.showAdvancedTooltips()) {
        tooltip.add(Lang.ITEM_FILTER_CONFIGURED.get(TextFormatting.ITALIC));
        tooltip.add(Lang.ITEM_FILTER_CLEAR.get(TextFormatting.ITALIC));
      }
    }
  }

  @Override
  @Nullable
  @SideOnly(Side.CLIENT)
  public GuiScreen getClientGuiElement(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, @Nullable EnumFacing facing, int param1) {
    // return new PowerItemFilterGui(player.inventory,
    // new ContainerFilter<IItemFilter>(player.inventory, param1, (TileEntityBase) world.getTileEntity(pos), facing), world.getTileEntity(pos));
    return null;
  }

}
