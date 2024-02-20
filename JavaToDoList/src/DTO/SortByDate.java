package DTO;

import java.util.Comparator;

/**
 * SortByDate : TeamTodoList 리스트를 날짜를 비교하기 위해
 * 
 * @author orbit
 */
public class SortByDate implements Comparator<TeamToDoList> {
	@Override
	public int compare(TeamToDoList a, TeamToDoList b) {
		return a.getClosedDate().compareTo(b.getClosedDate());
	}
}