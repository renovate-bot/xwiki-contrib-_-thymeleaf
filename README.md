# Thymeleaf Macro

<Short Description of Extension, taken from the description element in the pom.xml>

* Project Lead: [<info taken from the jira project, e.g. Vincent Massol>](<url to user profile on xwiki.org>)
  <if single extension page>
* [Documentation & Download](<url on e.x.o, e.g. https://extensions.xwiki.org/xwiki/bin/view/Extension/Flash+messages+application>)
  </if single extension page>
  <if several extension pages>
* Documentation & Downloads:
    * [<pretty name of page1, e.g. My App API](<url1 on e.x.o)
      ...
    * [<pretty name of pageN, e.g. My App API](<urlN on e.x.o)
      </if several extension pages>
* [Issue Tracker](<url on jira.xwiki.org, e.g. https://jira.xwiki.org/browse/XAFLASHM>)
* Communication: [Forum](<url, e.g. https://forum.xwiki.org></url>), [Chat](<url, e.g. https://dev.xwiki.org/xwiki/bin/view/Community/Chat>)
  <if link pointing to all dev practices>
* [Development Practices](<URL pointing to a site defining the list of practices to be followed by contributors when contributing on this project>)
  </if link pointing to all dev practices>
  <if no single link pointing to all dev practices>
* Development Practices:
    * <best practice 1, possibly with some link>
      ...
    * <best practice N, possibly with some link>
      </if no single link pointing to all dev practices>
* Minimal XWiki version supported: <taken from the pom.xml, e.g. XWiki 6.4.7>
* License: <license,taken from the pom.xml, e.g. LGPL 2.1>.
  <if translation is used>
* [Translations](<url on l10n to translations for this extension>)
  </if translation is used>
  <if translation is not used>
* Translations: N/A
  </if translation is not used>
  <if sonar is used>
* [Sonar Dashboard](<url to the project’s dashboard on sonar.xwiki.org, e.g. https://sonar.xwiki.org/dashboard/index/10464>)
  </if sonar is used>
  <if sonar is not used>
* Sonar Dashboard: N/A
  </if sonar is not used>
  <if ci is used>
* Continuous Integration Status: [![Build Status](https://ci.xwiki.org/job/XWiki%20Contrib/job/<job name on ci.xwiki.org>/job/master/badge/icon)](https://ci.xwiki.org/job/XWiki%20Contrib/job/<job name on ci.xwiki.org>/job/master/)
  </if ci is used>
  <if ci is not used>
* Continuous Integration Status: N/A
  </if ci is not used>

<optional> 
## Whatever 
...
</optional> 

## TODO:

- [ ] `${a.b}` does not fallback to `${a.get('b'}`
- [ ] `#{a.b}` should lookup for a translation key name `a.b`
- [] how to bind a velocity macro into thymeleaf