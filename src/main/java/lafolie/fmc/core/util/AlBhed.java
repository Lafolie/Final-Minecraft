package lafolie.fmc.core.util;

import java.util.HashMap;
import java.util.Map;

public abstract class AlBhed 
{
	private static final Map<Character, Character> ALBHED_CYPHER = new HashMap<>();
	private static final Map<Character, Character> ENGLISH_CYPHER = new HashMap<>();

	static
	{
		String alBhed =	 "EPSTIWKNUVGCLRYBXHMDOFZQAJ";
		String english = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		for(int n = 0; n < 26; n++)
		{
			ALBHED_CYPHER.put(english.charAt(n), alBhed.charAt(n));
			ENGLISH_CYPHER.put(alBhed.charAt(n), english.charAt(n));
		}
	}

	public static String toAlBhed(String str)
	{
		return translate(str, ALBHED_CYPHER);
	}

	public static String toEnglish(String str)
	{
		return translate(str, ENGLISH_CYPHER);
	}

	private static String translate(String str, Map<Character, Character> cypher)
	{
		StringBuilder builder = new StringBuilder();
		for(Character chr : str.toCharArray())
		{
			Character result = chr;
			Character upper = Character.toUpperCase(chr);
			if(cypher.containsKey(upper))
			{
				result = cypher.get(upper);
				result = Character.isLowerCase(chr) ? Character.toLowerCase(result) : result;
			}
			builder.append(result);
		}
		return builder.toString();
	}
}
