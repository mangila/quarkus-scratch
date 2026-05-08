package com.github.mangila.crud1.person.domain.model;

import com.github.mangila.crud1.shared.ApplicationException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import io.github.mangila.ensure4j.Ensure;
import io.github.mangila.ensure4j.ops.EnsureStringOps;

public record Phone(String number, String region, String type) {

  private static final EnsureStringOps ENSURE_STRING_OPS = Ensure.strings();
  private static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();

  public Phone {
    ENSURE_STRING_OPS.notBlank(number, "phone cannot be null or blank");
    ENSURE_STRING_OPS.notBlank(region, "region cannot be null or blank");
    ENSURE_STRING_OPS.notBlank(type, "type cannot be null or blank");
    region = region.toUpperCase();
    type = type.toUpperCase();
    number = PhoneNumberUtil.normalizeDigitsOnly(number);
    Phonenumber.PhoneNumber parsed;
    try {
      parsed = PHONE_NUMBER_UTIL.parse(number, region);
    } catch (NumberParseException e) {
      throw new ApplicationException(e);
    }
    Ensure.isTrue(PHONE_NUMBER_UTIL.isValidNumber(parsed), "phone number is not valid");
    number = PHONE_NUMBER_UTIL.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164);
  }

  public static Phone of(String number, String regionCode, String type) {
    return new Phone(number, regionCode, type);
  }

  public Phonenumber.PhoneNumber asPhoneNumber() throws NumberParseException {
    return PHONE_NUMBER_UTIL.parse(number, region);
  }
}
