package zy.dso;

import java.math.BigInteger;

/**
 * 保留pstyle、ilvl和ilfo的信息。
 * 
 * @author yangzhao
 */
public class PWithIndex {

	private String content = "";
	private BigInteger ilvl;
	private BigInteger ilfo;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public BigInteger getIlvl() {
		return ilvl;
	}
	public void setIlvl(BigInteger ilvl) {
		this.ilvl = ilvl;
	}
	public BigInteger getIlfo() {
		return ilfo;
	}
	public void setIlfo(BigInteger ilfo) {
		this.ilfo = ilfo;
	}
}
