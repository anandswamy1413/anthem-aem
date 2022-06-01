## On this Page 

* [Steps to run this project in your local](#markdown-header-steps-to-run-this-project-in-your-local)
* [Site Structure](#markdown-header-site-structure)
* [Repository Structure](#markdown-header-repository-structure)
* [AEM Architecture Deep dive or Refresher](#markdown-header-aem-architecture-deepdive-or-refresher)

## Steps to run this project in your local

* Download the AEM 6.5 instance from [here](https://drive.google.com/drive/folders/1oxqqAIrL5HkltC8hcXj6wc4BvLm2J1Fl?usp=sharing "Download the AEM 6.5 instance") & if you are unable to access this file , mail [me](mailto:titus@moonraft.com "Mail Titus") or [Chay](mailto:chaitanya.mullangi@moonraft.com "Mail Chaitanya")
* Clone this project in your local
* Run the following command

```
        mvn clean install -PautoInstallPackage
```
* If you wish to setup your instance in a MAC system , [check this out](https://github.com/TitusRobyK/Couple-of-obscure-Readmes/blob/main/mac-developer-guide.md)

## Site Structure

Adhere to the following site structure religiously, while creating new components,templates or content

```
root
├── apps
│   ├── anthemhr
│   │   └──hrportal
│   │      ├──── clientlibs
│   │      ├──── components
│   │      └──── install
│   └── vaccine-sites
│          ├──── clientlibs
│          ├──── components
│          └──── install
├── conf
│   ├── anthemhr
│   │   └──hrportal
│   │      └──── settings
│   │            ├──── cloudconfigs
│   │            ├──── dam
│   │            │     └──── cfm
│   │            └──── wcm
│   │                 ├──── templates
│   │                 ├──── policies
│   │                 └──── template-types
│   │
│   └── covid19
│       └──── settings
│             ├──── cloudconfigs
│             ├──── dam
│             │     └──── cfm
│             └──── wcm
│                  ├──── templates
│                  ├──── policies
│                  └──── template-types
└── content
    ├── dam
    │   ├──gatewaytoanthem
    │   └──covid19
    │
    │ 
    ├── gatewaytoanthem
    │   ├──api
    │   │  ├──── logout
    │   │  ├──── generate-ics
    │   │  ├──── ..
    │   │  └──── ..
    │   │
    │   ├──jcr:content
    │   └──beacon
    │      ├──en
    │      │  ├──── sign-in
    │      │  └──── home
    │      └──es
    │         ├──── sign-in
    │         └──── home
    │
    └── covid-sites
        ├──api
        │  ├──── vaccine-site-finder
        │  ├──── ..
        │  └──── ..
        │
        ├──empireblue
        ├──communitycareexplorer
        └──anthem
           └──── tng
                 └──── covid19
                       └──── vaccine-site-finder
```

## Repository Structure

This is the Repository Structure that we are following here. Kindly adhere to this said 
Repository Structure rules and regulations religiously. 
Any PR that depart from this established structure wont be merged at any cost.

```

root
├── all
│   ├── src/main/content/META-INF/vault
│   │   └──filter.xml
│   └── pom.xml                                            // In the event of a new independent sites ,be sure add the necessary entires in the pom.xml
│
├── dispatcher.ams                                         // Dispatcher Module
│   ├── src
│   │   ├──conf.d                                          // Enabled Vhosts, rewrite files are maintained here
│   │   ├──conf.dispatcher.d                               // Farm files, cache rules, clientheaders,filters are maintained in this folder
│   │   ├──conf.modules.d                                  // Do not make changes in this Module
│   │   └──conf                                            // Do not make changes in this Module
│   ├assembly.xml                                          // Assembling of dispatcher files based on the rules specified in this file
│   └pom.xml                                               // Dependencies & Build rules for the dispatcher module
│                                                               
├── hrportal        
│   ├── core
│   │   ├── src
│   │   │   ├──main/java/com/anthem/hrportal/core          // Services, Models & Servlets are maintained here
│   │   │   └──test
│   │   │      ├──resources/com/anthem/hrportal/core       // JSON files for the unit test cases     
│   │   │      └──java/com/anthem/hrportal/core            // Unit Test Cases             
│   │   └──pom.xml                                         // Dependencies & Build rules for the Core module
│   │
│   ├── ui.apps.structure
│   │   ├──src/main/resources/META-INF/MANIFEST.MF
│   │   └──pom.xml
│   │
│   ├── ui.apps
│   │   ├──src/main
│   │   │  ├──content
│   │   │  │  ├──jcr_root/apps/anthemhr/hrportal
│   │   │  │  │  │
│   │   │  │  │  ├──clientlibs                             // Base Client Library
│   │   │  │  │  ├──components                             // Components pertaining to hrportal are maintained here
│   │   │  │  │  └──install                                // Third Party JAR files needs to added here.
│   │   │  │  │
│   │   │  │  └──META-INF/vault/filter.xml                 // Rules that would determine, which folders/files needs to be installed in the AEM Instance   
│   │   │  └──resources/META-INF/MANIFEST.MF
│   │   └──pom.xml                                         // Dependencies & Build rules for the apps module
│   │
│   └── ui.content
│       ├──src/main
│       │  ├──content
│       │  │  ├──jcr_root
│       │  │  │  ├──conf/anthemhr/hrportal                  // Templates are maintained here                 
│       │  │  │  └──content/gatewaytoanthem                 // ResourceTypes and Nodes of various servlets are maintained here.
│       │  │  │
│       │  │  └──META-INF/vault/filter.xml                  // Rules that would determine, which folders/files needs to be installed in the AEM Instance 
│       │  └──resources/META-INF/MANIFEST.MF
│       └──pom.xml                                          // Dependencies & Build rules for the content module
│    
│    
├── vaccine-sites
│   ├── core
│   │   ├── src
│   │   │   ├──main/java/com/anthem/vaccine/sites/core
│   │   │   └──test
│   │   │      ├──resources/com/anthem/vaccine/sites/core       
│   │   │      └──java/com/anthem/vaccine/sites/core             
│   │   └──pom.xml
│   │
│   ├── ui.apps.structure
│   │   ├──src/main/resources/META-INF/MANIFEST.MF
│   │   └──pom.xml
│   │
│   ├── ui.apps
│   │   ├──src/main
│   │   │  ├──content
│   │   │  │  ├──jcr_root/apps/vaccine-sites
│   │   │  │  │  │
│   │   │  │  │  ├──clientlibs
│   │   │  │  │  ├──components
│   │   │  │  │  └──install
│   │   │  │  │
│   │   │  │  └──META-INF/vault/filter.xml   
│   │   │  └──resources/META-INF/MANIFEST.MF
│   │   └──pom.xml
│   │
│   ├── ui.content
│   │   ├──src/main
│   │   │  ├──content
│   │   │  │  ├──jcr_root
│   │   │  │  │  ├──conf/covid19
│   │   │  │  │  └──content/covid-sites
│   │   │  │  │
│   │   │  │  └──META-INF/vault/filter.xml   
│   │   │  └──resources/META-INF/MANIFEST.MF
│   │   └──pom.xml
│   │
│   └pom.xml
│
:
:
:
:
:
│
└pom.xml                     // Dependencies & Build rules for the whole project module
```

## AEM Architecture Deepdive or Refresher

* [AEM as Cloud Service](https://experienceleague.adobe.com/docs/experience-manager-learn/cloud-service/overview.html?lang=en#what%E2%80%99s-new)
* [Writing AEM Test Cases](https://www.youtube.com/watch?v=g5x6F8bUHj8)
* [AEM Dispatcher in the Cloud](https://experienceleague.adobe.com/docs/experience-manager-cloud-service/implementing/content-delivery/disp-overview.html#content-delivery)