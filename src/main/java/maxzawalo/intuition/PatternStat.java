package maxzawalo.intuition;

public class PatternStat {
	public int[] data;
	public int[] count;
	// private int hash;

	public PatternStat(int[] pattern) {
		data = new int[pattern.length];
		System.arraycopy(pattern, 0, data, 0, pattern.length);
		count = new int[pattern.length];
		// for (int i = 0; i < count.length; i++)
		// count[i] = 0;
	}

	public static int CreateHash(int[] pattern) {
		int hash = 0;
		for (int i = 0; i < pattern.length; i++)
			hash ^= pattern[i];
		return hash;
	}

	public int ChainCount() {
		return count[count.length - 1];
	}
	// public boolean Check(int[] pattern, int hash) {
	// if (this.hash != hash)
	// return false;
	//
	// return Check(pattern);
	// }

	// public boolean Check(int[] pattern) {
	// for (int i = 0; i < data.length; i++)
	// if (data[i] != pattern[i])
	// return false;
	//
	// count++;// Совпадает - инкрементируем статистику
	// return true;
	// }

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < data.length; i++)
			str += data[i] + "(" + count[i] + ")"
			// (i != 0 ? "(" + count[i - 1] + ")" : "")
					+ "\t";
		// str += "[" + ChainCount() + "]";
		return str;
	}
}