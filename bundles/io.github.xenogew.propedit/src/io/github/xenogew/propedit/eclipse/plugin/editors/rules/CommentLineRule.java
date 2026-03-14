package io.github.xenogew.propedit.eclipse.plugin.editors.rules;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class CommentLineRule extends EndOfLineRule {

  public CommentLineRule(String startSequence, IToken token) {

    super(startSequence, token);
  }

  public CommentLineRule(String startSequence, IToken token, char escapeCharacter) {

    super(startSequence, token, escapeCharacter);
  }

  @Override
  protected IToken doEvaluate(ICharacterScanner scanner, boolean resume) {

    if (scanner.getColumn() == 0) {
      return super.doEvaluate(scanner, resume);
    }
    return Token.UNDEFINED;
  }

  @Override
  protected IToken doEvaluate(ICharacterScanner scanner) {

    return doEvaluate(scanner, false);
  }

}
