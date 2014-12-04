Solr Phone Filter
===============

This plugin provides a solr filter which can be used to format phone numbers using [libphonenumber](http://code.google.com/p/libphonenumber/) library.

Currently this repo contains an eclipse project but will be changed soon. You can download the project and import it in your eclipse workspace.

## How to build

Export the project as a jar (let's say phonelibfilter.jar).

Navigate to Solr home directory (or if you are using multicores navigate to desired core folder).

Create a folder named `lib` (in the same folder which contains `conf` and `data` folders).

Copy `phonelibfilter.jar` (your exported jar) and `libphonenumber-7.0.1.jar` (this is the libphonenumber library, you can find it in `lib` directory of this project - or you can use your own) into the previously created `lib` folder.

Edit `solrconfig.xml` and add the following line

```xml
<lib dir="../lib/" regex=".*\.jar" />
```

Edit `schema.xml` and add the filter type:

```xml
<fieldType name="text_phonenumber" class="solr.TextField">
  <analyzer>
    <tokenizer class="solr.KeywordTokenizerFactory"/>
    <filter class="husart.solr.phone.filter.PhoneNumberFilterFactory" 
      region="US" 
      format="INTERNATIONAL" 
      skipInvalid="false" 
      skipError="true" 
    />
  </analyzer>
</fieldType>
```

You can now restart solr and test the field.

## Filter parameters

So far this filter accepts four parameters:

1. **region** - default region formatted as [ISO 3166-1 two-letter country code](http://www.iso.org/iso/english_country_names_and_code_elements)
2. **format** - number format. There are three formats: `INTERNATIONAL` (default), `NATIONAL` and `E164`.
3. **skipInvalid** - if set to true then invalid phone numbers will be skipped
4. **skipError** - if set to true then phone numbers contaiing errors will be skipped

