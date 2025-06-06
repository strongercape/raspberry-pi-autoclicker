package de.strongercape

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.Particle;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.Sound;

public class DeathParticles extends JavaPlugin implements Listener {

    private boolean particlesEnabled;
    private boolean soundEnabled;
    private Particle particleType;
    private int particleCount;
    private double particleSpread;
    private Sound deathSound;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        saveDefaultConfig(); // Erstellt die config.yml, falls sie nicht existiert
        loadConfig();
        getLogger().info("DeathParticles Plugin aktiviert!");
    }

    private void loadConfig() {
        FileConfiguration config = getConfig();
        particlesEnabled = config.getBoolean("particles.enabled", true);
        soundEnabled = config.getBoolean("sound.enabled", true);
        String particleTypeName = config.getString("particles.type", "REDSTONE").toUpperCase();
        particleType = parseParticleType(particleTypeName);
        particleCount = config.getInt("particles.count", 50);
        particleSpread = config.getDouble("particles.spread", 0.5);
        String soundName = config.getString("sound.type", "ENTITY_GENERIC_EXPLODE").toUpperCase();
        getLogger().info("Partikel aktiviert: " + particlesEnabled);
        getLogger().info("Sound aktiviert: " + soundEnabled);
        getLogger().info("Partikeltyp: " + particleType);
        getLogger().info("Partikelanzahl: " + particleCount);
        getLogger().info("Partikelstreuung: " + particleSpread);
        getLogger().info("Soundtyp: " + deathSound);
    }

    private Particle parseParticleType(String typeName) {
        try {
            return Particle.valueOf(typeName);
        } catch (IllegalArgumentException e) {
            getLogger().warning("Ungültiger Partikeltyp '" + typeName + "'. Verwende REDSTONE als Standard.");
            return Particle.REDSTONE;
        }
    }

    private Sound parseSound(String soundName) {
        try {
            return Sound.valueOf(soundName);
        } catch (IllegalArgumentException e) {
            getLogger().warning("Ungültiger Soundtyp '" + soundName + "'. Verwende ENTITY_GENERIC_EXPLODE als Standard.");
            return Sound.ENTITY_GENERIC_EXPLODE;
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location location = player.getLocation();

        if (particlesEnabled) {
            player.getWorld().spawnParticle(particleType, location, particleCount, particleSpread, particleSpread, particleSpread, 0);
        }

        if (soundEnabled && deathSound != null) {
            player.getWorld().playSound(location, deathSound, 1.0f, 1.0f);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("DeathParticles Plugin deaktiviert!");
    }
}