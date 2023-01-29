; Plumcode Setup

#define PlumcodeName "Plumcode"
#define PlumcodeVersion "0.5"
#define PlumcodePublisher "Klash Development"
#define PlumcodeURL "https://klashdevelopment.github.io/plumcode"
#define PlumcodeExeName "Plumcode"
#define PlumcodeExePath "K:\Projects\Plumcode\builds\finals\" + PlumcodeExeName
#define PlumcodeAssocName PlumcodeName + " Executable"
#define PlumcodeAssocExt ".plum"
#define PlumcodeAssocKey StringChange(PlumcodeAssocName, " ", "") + PlumcodeAssocExt

[Setup]
; NOTE: The value of AppId uniquely identifies this application. Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{A00CD7BD-12CD-49EE-9F2F-DC6DC07470CD}
AppName={#PlumcodeName}
AppVersion={#PlumcodeVersion}
AppVerName={#PlumcodeName} {#PlumcodeVersion}
AppPublisher={#PlumcodePublisher}
AppPublisherURL={#PlumcodeURL}
AppSupportURL={#PlumcodeURL}
AppUpdatesURL={#PlumcodeURL}
DefaultDirName={autopf}\{#PlumcodeName}
ChangesAssociations=yes
DisableProgramGroupPage=yes
PrivilegesRequiredOverridesAllowed=dialog
OutputDir=K:\Projects\Plumcode\builds\setup
OutputBaseFilename=PlumcodeSetup
SetupIconFile=C:\Users\Fox\Downloads\plumetRounded.ico
Compression=lzma
SolidCompression=yes

; change to 'classic' or 'modern'
WizardStyle=modern

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
Source: "{#PlumcodeExePath}"; DestDir: "{app}"; Flags: ignoreversion
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Registry]
Root: HKA; Subkey: "Software\Classes\{#PlumcodeAssocExt}\OpenWithProgids"; ValueType: string; ValueName: "{#PlumcodeAssocKey}"; ValueData: ""; Flags: uninsdeletevalue
Root: HKA; Subkey: "Software\Classes\{#PlumcodeAssocKey}"; ValueType: string; ValueName: ""; ValueData: "{#PlumcodeAssocName}"; Flags: uninsdeletekey
Root: HKA; Subkey: "Software\Classes\{#PlumcodeAssocKey}\DefaultIcon"; ValueType: string; ValueName: ""; ValueData: "{app}\{#PlumcodeExeName},0"
Root: HKA; Subkey: "Software\Classes\{#PlumcodeAssocKey}\shell\open\command"; ValueType: string; ValueName: ""; ValueData: """{app}\{#PlumcodeExeName}"" ""%1"""
Root: HKA; Subkey: "Software\Classes\Applications\{#PlumcodeExeName}\SupportedTypes"; ValueType: string; ValueName: ".myp"; ValueData: ""

[Icons]
Name: "{autoprograms}\{#PlumcodeName}"; Filename: "{app}\{#PlumcodeExeName}"
Name: "{autodesktop}\{#PlumcodeName}"; Filename: "{app}\{#PlumcodeExeName}"; Tasks: desktopicon

[Run]
Filename: "{app}\{#PlumcodeExeName}"; Description: "{cm:LaunchProgram,{#StringChange(PlumcodeName, '&', '&&')}}"; Flags: shellexec postinstall skipifsilent

