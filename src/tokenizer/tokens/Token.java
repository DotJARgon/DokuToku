package tokenizer.tokens;

public class Token {
	private String type, contents;
	
	public Token(String type, String contents) {
		this.type = type;
		this.contents = contents;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{ ");
		sb.append("type: ");
		sb.append(type);
		sb.append(", ");
		sb.append("contents: ");
		sb.append(contents);
		sb.append(" }");
		
		return sb.toString();
	}
	
}
