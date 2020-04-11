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
package kiwi.minecraft.cartjets.commands;

import java.util.List;
import java.util.Locale;
import kiwi.minecraft.cartjets.configuration.LanguageConfiguration;
import kiwi.minecraft.cartjets.utils.CurrentEntries;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

/**
 *
 * @author Joel
 */
public class CommandCartjets implements TabExecutor {

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    
    Locale locale =
      sender instanceof Player ? Locale.forLanguageTag(((Player) sender).getLocale()) : Locale.ENGLISH;
    locale = locale != null ? locale : Locale.ENGLISH;
    
    if (!sender.hasPermission("cartjets.commands.cartjets")) {
      sender.spigot().sendMessage(
        ComponentSerializer.parse(
          LanguageConfiguration.getInstance().getString(
            CurrentEntries.LANG_NO_PERM.toString()
            , locale)
        )
      );
      return true;
    }
      
      
      
      return true;
    
  }
}
