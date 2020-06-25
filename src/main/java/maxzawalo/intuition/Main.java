package maxzawalo.intuition;

public class Main {

	public static void main(String[] args) {
		PatternTree tree = new PatternTree();
		for (int i = 0; i < 100; i++)
			tree.Add("мама мыла раму");
		for (int i = 0; i < 20; i++)
			tree.Add("мама мыла милу мылом");
		for (int i = 0; i < 5; i++)
			tree.Add("мама мыла калькулятор");
		for (int i = 0; i < 1; i++)
			tree.Add("мама мыла магазин");
		System.out.println(tree.NextStep("мама мыла"));

		tree = new PatternTree();
		for (int i = 0; i < 21; i++)
			tree.Add("мама мыла милу мылом");
		for (int i = 0; i < 20; i++)
			tree.Add("мама мыла калькулятор");
		for (int i = 0; i < 5; i++)
			tree.Add("мама мыла раму");
		for (int i = 0; i < 1; i++)
			tree.Add("мама мыла магазин");
		System.out.println(tree.NextStep("мама мыла"));
	}
}