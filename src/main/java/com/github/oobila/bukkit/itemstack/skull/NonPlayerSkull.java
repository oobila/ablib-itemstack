package com.github.oobila.bukkit.itemstack.skull;

import com.github.oobila.bukkit.itemstack.CustomItemStack;
import com.github.oobila.bukkit.itemstack.CustomItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.oobila.bukkit.common.ABCommon.log;

/**
 * Non-player skull is a player head with a base64 texture string to give it a unique texture
 */
public class NonPlayerSkull extends CustomItemStack {

    private static final String URL_PREFIX = "http://textures.minecraft.net/texture/";
    private static final Pattern BASE64_PATTERN = Pattern.compile("\\\"http:\\/\\/textures\\.minecraft\\.net\\/texture\\/(.*)\\\"");

    @SuppressWarnings("java:S3011")
    public NonPlayerSkull(String textureId) {
        super(Material.PLAYER_HEAD);

        try {
            if(!textureId.isEmpty()) {
                SkullMeta headMeta = (SkullMeta) getItemMeta();
                setTexture(headMeta, textureId);
                setItemMeta(headMeta);
            }
        } catch (Exception e) {
            log(Level.WARNING, "Could not create custom skull");
        }
    }

    public static void setTexture(SkullMeta meta, URL skinUrl) {
        PlayerProfile profile = Bukkit.createPlayerProfile("oobila");
        PlayerTextures playerTextures = profile.getTextures();
        playerTextures.setSkin(skinUrl);
        profile.setTextures(playerTextures);
        meta.setOwnerProfile(profile);
    }

    public static void setTexture(SkullMeta meta, String textureId) {
        setTexture(meta, getUrl(textureId));
    }

    private static URL getUrl(String textureId){
        try {
            if (textureId.length() > 160) { //base64String
                byte[] decodedBytes = Base64.getDecoder().decode(textureId);
                Matcher matcher = BASE64_PATTERN.matcher(new String(decodedBytes));
                if (matcher.find()) {
                    textureId = matcher.group(1);
                }
            }
            textureId = textureId.replaceAll("[^0-9a-zA-Z]", "");
            return new URL(URL_PREFIX + textureId);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static CustomItemStackBuilder builder(String textureId) {
        return new CustomItemStackBuilder(new NonPlayerSkull(textureId));
    }
}