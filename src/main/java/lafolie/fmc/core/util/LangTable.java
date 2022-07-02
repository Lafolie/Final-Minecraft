package lafolie.fmc.core.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;

public class LangTable
{
	private Map<String, KeyColor> map = new HashMap<>();
	private int defaultColor = 0xFFFFFF;
	public String prefix = "";

	public static class KeyColor
	{
		public String key;
		public int color;
		public Style style = Style.EMPTY;

		public KeyColor(String key, int color)
		{
			this.key = key;
			this.color = color;
			style = style.withColor(TextColor.fromRgb(color));
		}
	}

	public LangTable() {}

	public LangTable(String prefix)
	{
		this.prefix = prefix;
	}

	public LangTable(int defaultColor)
	{
		this.defaultColor = defaultColor;
	}

	public LangTable(String prefix, int defaultColor)
	{
		this.prefix = prefix;
		this.defaultColor = defaultColor;
	}

	public void addPrefixed(String name)
	{
		addPrefixed(name, defaultColor);
	}

	public void addPrefixed(String name, int color)
	{
		add(name, prefix.concat(name), color);
	}

	public void add(String name, String key)
	{
		add(name, key, defaultColor);
	}

	public void add(String name, String key, int color)
	{
		map.put(name, new KeyColor(key, color));
	}

	public KeyColor get(String name)
	{
		return map.get(name);
	}

	public Text getText(String name)
	{
		KeyColor kc = map.get(name);
		return new TranslatableText(kc.key).fillStyle(kc.style);
	}

	public Text getText(String name, Object... args)
	{
		KeyColor kc = map.get(name);
		return new TranslatableText(kc.key, args).fillStyle(kc.style);
	}
}
