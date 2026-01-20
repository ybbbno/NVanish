package me.ybbbno.nvanish.pm;

import me.deadybbb.ybmj.PluginProvider;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.translation.Translator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class PMTranslator implements Translator {
    private final PluginProvider plugin;

    public PMTranslator(PluginProvider plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull Key name() {
        return Key.key("ybmj:pm_translator");
    }

    @Override
    public @Nullable MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
        String pattern = null;
        boolean isRussian = locale.getLanguage().equals("ru");
        pattern = switch (key) {
            case PMTranslationKeys.WHISPERS_TO -> isRussian ? "Вы прошептали {0}: {1}" : "You whisper to {0}: {1}";
            case PMTranslationKeys.WHISPERS_FROM -> isRussian ? "{0} шепчет вам: {1}" : "{0} whispers to you: {1}";
            case PMTranslationKeys.UNKNOWN_PLAYER -> isRussian ? "Игрок не обнаружен" : "No player was found";
            default -> pattern;
        };

        if (pattern != null) {
            return new MessageFormat(pattern, locale);
        } else {
            return null;
        }
    }

    @Override
    public @Nullable Component translate(@NotNull TranslatableComponent component, @NotNull Locale locale) {
        MessageFormat format = translate(component.key(), locale);
        if (format == null) {
            return null;
        }

        Object[] arguments = component.arguments().stream()
                .map(arg -> LegacyComponentSerializer.legacySection().serialize(arg.asComponent()))
                .toArray();

        String formattedText = format.format(arguments);

        Component result = LegacyComponentSerializer.legacySection().deserialize(formattedText);

        for(Map.Entry<TextDecoration, TextDecoration.State> a :  component.decorations().entrySet().stream()
                .filter(entry -> entry.getValue() == TextDecoration.State.TRUE)
                .collect(Collectors.toSet())) {
            result = result.decorate(a.getKey());
        }

        result = result.color(component.color());

        return result;
    }
}
