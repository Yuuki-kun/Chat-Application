package application;

import java.util.ArrayList;

import searchalgorithm.Search;

public class test_Search {
	static ArrayList<String> nameCollection = new ArrayList<>();
	public static void main(String[] args) {
		nameCollection.add("Tong Cong Minh");
		nameCollection.add("Tran Manh Huynh");
		nameCollection.add("Ngoc Anh");
		nameCollection.add("Hoang Ly");
		nameCollection.add("Manh Huynh");
		
		ArrayList<String> result = new ArrayList<>();
		result = Search.searchIboxByName("Tong Cong Minh", nameCollection);
		
		for(String name : result) {
			System.out.println(name);
		}
	}
}
