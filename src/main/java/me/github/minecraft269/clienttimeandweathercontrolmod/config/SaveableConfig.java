package me.github.minecraft269.clienttimeandweathercontrolmod.config;

public class SaveableConfig<T> {

    public final T config;
    public final String key;

    public SaveableConfig(String key, T config) {
        this.key = key;
        this.config = config;
    }

    public static <C> SaveableConfig<C> fromConfig(String key, C config) {
        return new SaveableConfig<>(key, config);
    }
}