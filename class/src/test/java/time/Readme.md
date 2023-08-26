# Java8 中有关时间的类

Java中时间戳为0时表示的时间是1970-01-01 00:00:00 由于 Date类是和时区有关的，所以实例化的时候会带上时区信息，格式化输出也会带上时区
但是System.currentTimeMillis()就是时区无关的时间戳。
以及Java8新增的LocalDateTime等类of()方式实例化都是不带时区信息，但是now()方式实例化不一样，会依据当前时间戳以及时区信息转换得到相对时间戳

```java
    System.out.println(new Date(0)); // Thu Jan 01 08:00:00 CST 1970
```

- 时区有关 Date
- 时区无关 LocalDateTime LocalDate LocalTime

- java.time 包下

```
├── chrono/
│         ├── AbstractChronology
│         ├── ChronoLocalDate
│         ├── ChronoLocalDateImpl
│         ├── ChronoLocalDateTime
│         ├── ChronoLocalDateTimeImpl
│         ├── Chronology
│         ├── ChronoPeriod
│         ├── ChronoPeriodImpl
│         ├── ChronoZonedDateTime
│         ├── ChronoZonedDateTimeImpl
│         ├── Era
│         ├── HijrahChronology 伊斯兰教日历
│         ├── HijrahDate
│         ├── HijrahEra
│         ├── IsoChronology
│         ├── IsoEra
│         ├── JapaneseChronology 日本
│         ├── JapaneseDate
│         ├── JapaneseEra
│         ├── MinguoChronology  民国
│         ├── MinguoDate
│         ├── MinguoEra
│         ├── Ser
│         ├── ThaiBuddhistChronology 泰国佛教日历
│         ├── ThaiBuddhistDate
│         └── ThaiBuddhistEra
├── format/
│         ├── DateTimeFormatterBuilder
│         ├── DateTimeFormatter
│         ├── DateTimeParseContext
│         ├── DateTimeParseException
│         ├── DateTimePrintContext
│         ├── DateTimeTextProvider
│         ├── DecimalStyle
│         ├── FormatStyle
│         ├── Parsed
│         ├── ResolverStyle
│         ├── SignStyle
│         ├── TextStyle
│         └── ZoneName
├── temporal/
│         ├── ChronoField
│         ├── ChronoUnit
│         ├── IsoFields
│         ├── JulianFields
│         ├── TemporalAccessor
│         ├── TemporalAdjuster
│         ├── TemporalAdjusters
│         ├── TemporalAmount
│         ├── Temporal
│         ├── TemporalField
│         ├── TemporalQueries
│         ├── TemporalQuery
│         ├── TemporalUnit
│         ├── UnsupportedTemporalTypeException
│         ├── ValueRange
│         └── WeekFields
├── zone/
│         ├── Ser
│         ├── TzdbZoneRulesProvider
│         ├── ZoneOffsetTransition
│         ├── ZoneOffsetTransitionRule
│         ├── ZoneRules
│         ├── ZoneRulesException
│         └── ZoneRulesProvider
├── Clock
├── DateTimeException
├── DayOfWeek
├── Duration
├── Instant
├── LocalDate
├── LocalDateTime
├── LocalTime
├── Month
├── MonthDay
├── OffsetDateTime
├── OffsetTime
├── Period
├── Ser
├── Year
├── YearMonth
├── ZonedDateTime
├── ZoneId
├── ZoneOffset
└── ZoneRegion
```