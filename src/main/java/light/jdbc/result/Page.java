package light.jdbc.result;

import java.util.List;

/**
 * 分页
 * @author hanjiang.Yue
 * @param <M>
 */
public class Page <M>{

	private Integer count;
	
	private List<M> list;

	public Integer getCount() {
		return count;
	}

	public List<M> getList() {
		return list;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setList(List<M> list) {
		this.list = list;
	}

	public Page(Integer count, List<M> list) {
		super();
		this.count = count;
		this.list = list;
	}

	public Page() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
}
