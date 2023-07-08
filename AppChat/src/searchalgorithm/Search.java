package searchalgorithm;

import java.util.ArrayList;
import java.util.Collections;

public class Search {
	public static ArrayList<String> searchIboxByName(String searchName, ArrayList<String> inboxF) {
		ArrayList<String> resultList = new ArrayList<>();

		Collections.sort(inboxF);

		int n = inboxF.size();
		int step = (int) Math.floor(Math.sqrt(n));
		int prev = 0;

		while (inboxF.get(Math.min(step, n) - 1).compareTo(searchName) < 0) {
			prev = step;
			step += (int) Math.floor(Math.sqrt(n));
			if (prev >= n)
				return resultList;
		}

		while (inboxF.get(prev).compareTo(searchName) < 0) {
			prev++;
			if (prev == Math.min(step, n))
				return resultList;
		}


		while (inboxF.get(prev).equals(searchName)) {
			resultList.add(inboxF.get(prev));
			prev++;
			if (prev == Math.min(step, n))
				return resultList;
		}

		// Kiểm tra các phần tử trước prev trong danh sách inboxF
		while (prev > 0 && inboxF.get(prev - 1).equals(searchName)) {
			prev--;
			resultList.add(inboxF.get(prev));
		}

		return resultList;
	}
}
