package maxzawalo.intuition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;


public class PatternTree {
	int count = 0;

	public static List<PatternStat> allPatterns = new ArrayList<>();
	Map<Integer, PatternTree> children = new HashMap<>();

	public PatternTree() {
	}

	// PatternStat pattern;
	void Search(int[] pattern, int pos, int[] countA, boolean setCount) {
		if (setCount)
			count = countA[pos];
		else
			count++;
		if (pos != pattern.length - 1) {// Не последний элемент в паттерне
			PatternTree child = children.get(pattern[pos + 1]);
			if (child == null) {
				child = new PatternTree();
				children.put(pattern[pos + 1], child);
			}
			child.Search(pattern, pos + 1, countA, setCount);
		}
	}

	int psize = 10;

	void FromPatterns(List<PatternStat> patterns) {
		for (PatternStat ps : patterns)
			Add(ps);
	}

	void Add(PatternStat pattern) {
		Add(pattern.data, pattern.count);
	}

	/*
	 * Добавляем в корень
	 */
	public void Add(int... pattern) {
		Add(pattern, new int[] {});
	}

	Map<Integer, Image> actions = new HashMap<>();

	public void Add(Image... pattern) {
		int[] arr = getPatternIds(pattern);
		for (int i = 0; i < pattern.length; i++)
			if (!actions.containsKey(pattern[i].id))
				actions.put(pattern[i].id, pattern[i]);

		Add(arr, new int[] {});
	}

	private int[] getPatternIds(Image... pattern) {
		int[] arr = new int[pattern.length];
		for (int i = 0; i < pattern.length; i++)
			arr[i] = pattern[i].id;
		return arr;
	}

	public void Add(String ph) {
		Add(GetWordIds(words, ph), new int[] {});
	}

	public void Add(int[] pattern, int[] count) {
		psize = pattern.length;
		PatternTree child = children.get(pattern[0]);
		if (child == null) {
			child = new PatternTree();
			children.put(pattern[0], child);
		}
		child.Search(pattern, 0, count, count.length != 0);
	}

	public void ToPatternList() {

		allPatterns.clear();

		PatternStat pattern = new PatternStat(new int[psize]);
		while (true) {
			pattern = new PatternStat(new int[psize]);
			int cnt = getPattern(this, 0, pattern);
			allPatterns.add(pattern);
			if (cnt == 0)
				break;
		}
	}

	private int getPattern(PatternTree tree, int pos, PatternStat pattern) {
		if (pos != 0)// Начальное дерево
			pattern.count[pos - 1] = tree.count;

		if (pos == psize) {// Последний элемент в паттерне
			return 0;
		}

		if (tree.children.keySet().size() != 0) {
			int id = tree.children.keySet().stream().findFirst().get();
			pattern.data[pos] = id;
			PatternTree t = tree.children.get(id);
			if (getPattern(t, pos + 1, pattern) == 0)
				tree.children.remove(id);
		}
		return tree.children.keySet().size();
	}

	public void Print() {
		allPatterns.removeIf(p -> p.ChainCount() < 2);
		Collections.sort(allPatterns, new Comparator<PatternStat>() {
			@Override
			public int compare(final PatternStat p1, final PatternStat p2) {
				int c = 0;
				for (int i = p1.count.length - 1; i >= 0; i--) {
					if (c == 0)
						c = ((Integer) p2.count[i]).compareTo(p1.count[i]);
				}
				return c;
			}
		});

		System.out.println("count=" + allPatterns.size());

		for (PatternStat ps : allPatterns)
			System.out.println(ps);
	}

	public int[] NextStep(int[] step) {
		return NextStep(step, new ArrayList<>());
	}

	public int[] NextStep(int[] step, List<Integer> variants) {
		List<Integer> ret = new ArrayList<>();
		PatternTree nextStep = this;// children.get(step[0]);
		String out = "";
		for (int i = 0; i < step.length; i++) {
			int id = step[i];
			out += id + "(" + nextStep.children.get(id).count + ")" + ",";
			nextStep = nextStep.children.get(id);
			ret.add(id);
		}
		out = removeLastChar(out);
		out += "->";

		// for (int id : nextStep.children.keySet()) {
		// if (nextStep.children.size() != 1)
		// out += id + "(" + nextStep.children.get(id).count + ")" + "|";
		// }
		while (nextStep.children.size() != 0 && variants.size() == 0) {
			// Если только один вариант - светим всю цепочку (до конца или до ветвления)
			while (nextStep.children.size() == 1) {
				int id = nextStep.children.keySet().stream().findFirst().get();
				out += id + "(" + nextStep.children.get(id).count + ")" + ",";
				nextStep = nextStep.children.get(id);
				ret.add(id);
			}

			// Если несколько вариантов - берем более вероятный
			while (nextStep.children.size() > 1) {
				List<Entry<Integer, PatternTree>> sorted = nextStep.children.entrySet().stream()//
						.sorted((o1, o2) -> ((Integer) o2.getValue().count).compareTo(o1.getValue().count))//
						.collect(Collectors.toList());
				int id = sorted.get(0).getKey();
				// Если варианты равновероятны - показываем только следующий шаг(варианты)
				// (TODO: цепочки?)
				if (sorted.get(0).getValue().count == sorted.get(sorted.size() - 1).getValue().count) {
					variants.addAll(sorted.stream().map(p -> p.getKey()).collect(Collectors.toList()));
					break;
				}
				out += id + "(" + nextStep.children.get(id).count + ")" + ",";
				nextStep = nextStep.children.get(id);
				ret.add(id);
			}
		}
		out = removeLastChar(out);
		System.out.println(out);
		// System.out.println(Arrays.toString(nextStep.children.keySet().toArray()));
		int[] r = new int[ret.size()];
		for (int i = 0; i < r.length; i++)
			r[i] = ret.get(i);
		return r;
	}

	public String NextStep(String ph) {
		return GetPhrase(words, NextStep(GetWordIds(words, ph)));
	}

	public Image[] NextStep(Image[] start, List<Image> variants) {
		int[] arr = getPatternIds(start);
		List<Integer> vars = new ArrayList<>();
		arr = NextStep(arr, vars);
		List<Image> out = new ArrayList<>();
		int i = 0;
		for (; i < arr.length; i++)
			out.add(actions.get(arr[i]));

		for (int j = 0; j < vars.size(); j++)
			variants.add(actions.get(vars.get(j)));

		return out.toArray(new Image[arr.length]);
	}

	public static String removeLastChar(String s) {
		return Optional.ofNullable(s).filter(str -> str.length() != 0).map(str -> str.substring(0, str.length() - 1))
				.orElse(s);
	}

	Map<Integer, String> words = new HashMap<>();

	String GetPhrase(Map<Integer, String> words, int[] arr) {
		String s = "";
		for (int i = 0; i < arr.length; i++)
			s += words.get(arr[i]) + " ";
		return s.trim();
	}

	int[] GetWordIds(Map<Integer, String> words, String str) {
		String[] ws = str.split(" ");
		int[] arr = new int[ws.length];
		for (int i = 0; i < arr.length; i++) {
			String word = ws[i].trim().toLowerCase();
			Optional<Entry<Integer, String>> val = words.entrySet()//
					.stream()//
					.filter(a -> a.getValue().equals(word))//
					.findFirst();
			if (val.isPresent())
				arr[i] = val.get().getKey();
			else {
				int newId = words.size() + 1;
				arr[i] = newId;
				words.put(newId, word);
			}
		}
		return arr;
	}
}