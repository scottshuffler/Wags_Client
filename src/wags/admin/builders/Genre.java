package wags.admin.builders;

public enum Genre {
	TRAVERSAL ("traversal"),
	HEAP_INSERT ("heapInsert"),
	HEAP_DELETE ("heapDelete"),
	RADIX ("radix"),
	MST ("mst"),
	HASHING ("hashing"),
	Q_SORT ("qsort"),
	REDBLACK ("redblack"),
	SELECTION_SORT ("selectionsort"),
	SIMPLE_PARTITION ("simplepartition"),
	GRAPHS ("graphs");
	
	private String key;
	
	Genre(String key){
		this.key = key;
	}
	
	@Override
	public String toString() {
		return key;
	}
}
