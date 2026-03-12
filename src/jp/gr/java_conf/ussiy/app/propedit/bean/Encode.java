package jp.gr.java_conf.ussiy.app.propedit.bean;

/**
 * Immutable data carrier for encoding information.
 *
 * @param no   ordinal index used for default-selection logic
 * @param name display name shown in the UI
 * @param code Java charset/encoding identifier
 */
public record Encode(int no, String name, String code) {

	@Override
	public String toString() {

		return name();
	}
}