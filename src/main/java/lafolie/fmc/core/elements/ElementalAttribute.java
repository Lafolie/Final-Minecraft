package lafolie.fmc.core.elements;

public enum ElementalAttribute
{
	WEAKNESS("FMC_ElementalWeakness"),
	RESISTANCE("FMC_ElementalResistance"),
	ABSORBTION("FMC_ElementalAbsorbtion");

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
