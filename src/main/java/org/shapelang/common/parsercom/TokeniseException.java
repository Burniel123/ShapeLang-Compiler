class TokeniseException extends Exception
{
	public TokeniseException(String s)
	{
		super(s);
		System.out.println("You're probably missing a space or linebreak somewhere");
		System.out.println("See the below stack trace for more info (line number tends to be very helpful");
	}
}

