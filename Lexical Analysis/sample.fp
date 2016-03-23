{Program Sample1
{Function facto VAL
{if {< VAL 0 }
then {= retVal -1}
else {= retVal 1}
{while {> VAL 0} do
{= retVal {* retVal VAL}}
{= VAL {- VAL 1}}
}
}
return retVal
}
{print {facto 999}}
}
