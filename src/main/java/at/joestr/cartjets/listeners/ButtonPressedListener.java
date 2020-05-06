//
// MIT License
// 
// Copyright (c) 2020 minecraft.kiwi
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
// 
package at.joestr.cartjets.listeners;

import at.joestr.cartjets.utils.MessageHelper;
import at.joestr.cartjets.CartJetsPlugin;
import at.joestr.cartjets.models.CartJetsModel;
import at.joestr.cartjets.configuration.CurrentEntries;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Level;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

/**
 *
 * @author Joel
 */
public class ButtonPressedListener implements Listener {
  
  private static final Material[] BUTTONS = new Material[] {
    Material.OAK_BUTTON,
    Material.STONE_BUTTON,
    Material.DARK_OAK_BUTTON,
    Material.ACACIA_BUTTON,
    Material.SPRUCE_BUTTON,
    Material.BIRCH_BUTTON,
    Material.JUNGLE_BUTTON
  };
  
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onCartJetButtonClick(PlayerInteractEvent ev) {
    Block clickedBlock = ev.getClickedBlock();
    if (clickedBlock == null) return;
    
    Material clickedBlockMaterial = clickedBlock.getType();
    if (!Arrays.stream(BUTTONS).anyMatch(clickedBlockMaterial::equals)) return;
    
    List<CartJetsModel> cartJets = null;
    try {
      cartJets =
        CartJetsPlugin.getInstance().getCartJetsDao().queryForAll();
    } catch (SQLException ex) {
      CartJetsPlugin.getInstance().getLogger().log(Level.SEVERE, null, ex);
    }
    if (cartJets == null) return;
    
    boolean buttonPresent =
      cartJets.stream()
        .anyMatch((b) -> {
          return b.getButtonLocation().equals(clickedBlock.getLocation());
        });
    if (buttonPresent) return;
    
    Optional<CartJetsModel> cartJet =
      cartJets.stream()
        .filter((b) -> {
          return b.getButtonLocation().equals(clickedBlock.getLocation());
        })
        .findFirst();
    
    Entity spawnedMinecart =
      ev.getPlayer().getWorld().spawnEntity(cartJet.get().getMinecartSpawningLocation(),
        EntityType.MINECART
      );
    
    spawnedMinecart.getPassengers().add(ev.getPlayer());
    
    spawnedMinecart.setMetadata(
      "cartjet.is",
      new FixedMetadataValue(CartJetsPlugin.getInstance(), true)
    );
    
    spawnedMinecart.setVelocity(new Vector(1, 1, 1));
    
    ev.setCancelled(true);
  }
  
  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onButtonClicked(PlayerInteractEvent ev) {
    Block clickedBlock = ev.getClickedBlock();
    if (clickedBlock == null) return;
    
    if (!CartJetsPlugin.getInstance().getPerUserModels().containsKey(ev.getPlayer().getUniqueId()))
      return;
    
    if (CartJetsPlugin.getInstance().getPerUserModels().get(ev.getPlayer().getUniqueId()).getButtonLocation() != null)
      return;
    
    Material clickedBlockMaterial = clickedBlock.getType();
    if (!Arrays.stream(BUTTONS).anyMatch(clickedBlockMaterial::equals)) return;
    
    List<CartJetsModel> cartJets = null;
    try {
      cartJets =
        CartJetsPlugin.getInstance().getCartJetsDao().queryForAll();
    } catch (SQLException ex) {
      CartJetsPlugin.getInstance().getLogger().log(Level.SEVERE, null, ex);
    }
    if (cartJets == null) return;
    
    boolean buttonPresent =
      cartJets.stream()
        .anyMatch((b) -> {
          return b.getButtonLocation().equals(clickedBlock.getLocation());
        });
    if (!buttonPresent) return;
    
    Optional<CartJetsModel> cartJet =
      cartJets.stream()
        .filter((b) -> {
          return b.getButtonLocation().equals(clickedBlock.getLocation());
        })
        .findFirst();
    
    Locale l = Locale.forLanguageTag(ev.getPlayer().getLocale());
    final Locale locale = l != null ? l : Locale.ENGLISH;
    
    if (!cartJet.isPresent()) {
      new MessageHelper()
        .path(CurrentEntries.LANG_CMD_CARTJETS_SETUPWIZARD_BUTTON_OVERLAPPING)
        .locale(locale)
        .receiver(ev.getPlayer())
        .send();
      ev.setCancelled(true);
      return;
    }
    
    CartJetsPlugin.getInstance().getPerUserModels().get(ev.getPlayer().getUniqueId())
      .setButtonLocation(ev.getClickedBlock().getLocation());
    new MessageHelper()
      .path(CurrentEntries.LANG_CMD_CARTJETS_SETUPWIZARD_BUTTON_SUCCESS)
      .locale(locale)
      .receiver(ev.getPlayer())
      .send();
    ev.setCancelled(true);
  }
}
