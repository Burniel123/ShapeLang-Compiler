
public class Block implements StmtType
{
	@Override
	public ParserToken stmtType() {
		return ParserToken.BLOCK;
	}

	public final Shape[] shapes;
}
