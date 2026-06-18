package zeta.client;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ZetaTweaker implements ITweaker {

    private final List<String> args = new ArrayList<>();

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        this.args.addAll(args);
        if (gameDir != null) {
            this.args.add("--gameDir");
            this.args.add(gameDir.getAbsolutePath());
        }
        if (assetsDir != null) {
            this.args.add("--assetsDir");
            this.args.add(assetsDir.getAbsolutePath());
        }
        if (profile != null) {
            this.args.add("--version");
            this.args.add(profile);
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        System.out.println("[Zeta Tweaker] injectIntoClassLoader called");
        MixinBootstrap.init();
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        Mixins.addConfiguration("mixins.zeta.json");
        System.out.println("[Zeta Tweaker] Mixins.addConfiguration done");

        removeClassLoaderExclusions();
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        List<String> clean = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            String arg = args.get(i);
            if (arg.equals("--tweakClass")) {
                i++;
                continue;
            }
            if (arg.equals("--accessToken") || arg.equals("--version")
                || arg.equals("--userProperties") || arg.equals("--assetIndex")
                || arg.equals("--assetsDir") || arg.equals("--gameDir")
                || arg.equals("--username") || arg.equals("--uuid")
                || arg.equals("--userType")) {
                clean.add(arg);
                if (i + 1 < args.size() && !args.get(i + 1).startsWith("--")) {
                    clean.add(args.get(++i));
                }
            }
        }
        return clean.toArray(new String[0]);
    }

    private static void removeClassLoaderExclusions() {
        try {
            Field f = LaunchClassLoader.class.getDeclaredField("classLoaderExceptions");
            f.setAccessible(true);
            Set<String> exclusions = (Set<String>) f.get(Launch.classLoader);
            if (exclusions != null) {
                exclusions.removeIf(s -> s.startsWith("zeta"));
                System.out.println("[Zeta Tweaker] Removed zeta exclusions from classLoaderExceptions");
            }
        } catch (Exception e) {
            System.out.println("[Zeta Tweaker] Could not modify classLoaderExceptions: " + e);
        }
    }
}
