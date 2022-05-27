package lafolie.fmc.core.elements;

public enum ElementalAttribute
{
	WEAKNESS("ElementalWeakness"),
	RESISTANCE("ElementalResistance"),
	ABSORBTION("ElementalAbsorbtion");

	private String key;

	public String toNbtKey()
	{
		return key;
	}

	private ElementalAttribute(String str)
	{
		key = str;
	}
}
