package com.github.mangila.web1.person.domain.model;

import com.github.mangila.web1.person.domain.PersonException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import io.github.mangila.ensure4j.Ensure;
import java.util.Locale;

public record Phone(String number, String region, String type) {

  private static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();

  public Phone {
    Ensure.notBlank(number, "phone cannot be null or blank");
    Ensure.notBlank(region, "region cannot be null or blank");
    Ensure.notBlank(type, "type cannot be null or blank");
    region = region.toUpperCase(Locale.ROOT);
    type = type.toUpperCase(Locale.ROOT);
    number = PhoneNumberUtil.normalizeDigitsOnly(number);
    final Phonenumber.PhoneNumber parsed = parsePhoneNumberOrThrow(number, region);
    Ensure.isTrue(PHONE_NUMBER_UTIL.isValidNumber(parsed), "phone number is not valid");
    number = PHONE_NUMBER_UTIL.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164);
  }

  public static Phone newInstance(String number, String regionCode, String type) {
    return new Phone(number, regionCode, type);
  }

  private static Phonenumber.PhoneNumber parsePhoneNumberOrThrow(String number, String region) {
    try {
      return PHONE_NUMBER_UTIL.parse(number, region);
    } catch (NumberParseException e) {
      throw new PersonException(e);
    }
  }
}
