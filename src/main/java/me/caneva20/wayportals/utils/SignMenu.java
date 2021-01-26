package me.caneva20.wayportals.utils;

import java.util.List;
import java.util.function.BiPredicate;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Singleton
public class SignMenu {

  private final SignMenuFactory factory;

  @Inject
  public SignMenu(SignMenuFactory factory) {
    this.factory = factory;
  }

  public void getInput(Player player, String title, BiPredicate<Player, String> onInput) {
    getInput(player, title, "", "", Material.DARK_OAK_SIGN, onInput);
  }

  public void getInput(Player player, String title, String defaultInput,
      BiPredicate<Player, String> onInput) {
    getInput(player, title, defaultInput, "", Material.DARK_OAK_SIGN, onInput);
  }

  public void getInput(Player player, String title, Material material,
      BiPredicate<Player, String> onInput) {
    getInput(player, title, "", "", material, onInput);
  }

  public void getInput(Player player, String title, String defaultInput, Material material,
      BiPredicate<Player, String> onInput) {
    getInput(player, title, defaultInput, "", material, onInput);
  }

  public void getInput(Player player, String title, String defaultInput, String extra,
      Material material, BiPredicate<Player, String> onInput) {
    factory.newMenu(List.of(defaultInput, "^ ^ ^ ^ ^ ^ ^ ^ ^", "§1" + title, extra))
        .material(material).response((p, s) -> onInput.test(p, ChatColor.stripColor(s[0])))
        .open(player);
  }
}
