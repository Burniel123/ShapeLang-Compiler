package Common.ParserCom;

import java.util.Optional;
import Common.parsercom;

public class Loop implements StmtType
{
	@Override
	public ParserToken stmtType()
	{
		return ParserToken.LOOP;
	}

	public final Optional<Integer> numIter; // None indicates infinite loop
	public final Text contents;
}
