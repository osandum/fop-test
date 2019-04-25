Test code to expose a weakness or bug in Apache FOP

Two modules, both contain the same unit test:
  1. conf/ builds fop-conf.jar containing the FOP config.xml with a sample font
  2. exec/ uses fop-conf.jar

Running `mvn install` will work in conf/, where config and fonts are loaded from
the classes folder (from `file:/.../target/classes/com/example/....`), but will
fail in exec/, where config and fonts are loaded from the fop-conf.jar (from
`jar:file:/.../fop-conf.jar!/com/example/....`)
