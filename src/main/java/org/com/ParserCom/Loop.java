package Common.ParserCom;

import java.util.Optional;

public class Loop implements StmtType
{
	@Override
	public ParserToken stmtType()
	{
		return LOOP;
	}

	public final Optional<Integer> numIter; // None indicates infinite loop
	public final Text contents;
}
