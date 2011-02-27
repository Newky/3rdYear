#!/usr/bin/perl
use strict;
use warnings;

# 0A = Continent
# 1B = Blank
# 2C = Country
# 3D = MOE
# 4E = Educational Portal
# 5F = Policies And Plans
# 6G = Notes 

my @continents = (
	"ASIA",
	"EUROPE",
	"AFRICA", 
	"N.AMER",
	"S.AMER", 
	"OCEAN"
);

my $con_id = -1;
my $country_name = "";
my $country_id = 0;
while (<>) {
	my @parts = split(/,/);
	unless($parts[0] eq ""){
		for (my $var = 0; $var <= $#continents; $var++) {
			if($continents[$var] eq $parts[0]){
				$con_id = $var;
				last;
			}
		}
	}
	unless(not defined($parts[2]) or $parts[2] eq ""){
		$country_name = $parts[2];
		unless($country_name =~ m/last updated/){
			$country_id +=1;
			printf "INSERT INTO country (id, cont_id, name, last_updated) VALUES ('%d','%d', \"%s\", 'NOW()');\n", $country_id, $con_id, $country_name;
		}
	}
	unless(not defined($parts[3]) or $parts[3] eq ""){
		$parts[3] =~ s/<COMMA>/,/g;
		chomp($parts[3]);
		printf "INSERT INTO ministry (id, country_id, website) VALUES(NULL, '%d', \"%s\");\n", $country_id, escape_string($parts[3]); 
	}
	unless(not defined($parts[4]) or $parts[4] eq ""){
		$parts[4] =~ s/<COMMA>/,/g;
		chomp($parts[4]);
		printf "INSERT INTO education_portal (id, country_id, website) VALUES(NULL, '%d', \"%s\");\n", $country_id, escape_string($parts[4]); 
	}
	unless(not defined($parts[5]) or $parts[5] eq ""){
		$parts[5] =~ s/<COMMA>/,/g;
		chomp($parts[5]);
		printf "INSERT INTO policies (id, country_id, website) VALUES(NULL, '%d', \"%s\");\n", $country_id, escape_string($parts[5]); 
	}
	if(defined($parts[6])){
		unless($parts[6] eq ""){
			$parts[6] =~ s/<COMMA>/,/g;
			chomp($parts[6]);
			printf "INSERT INTO content (id, country_id, comments) VALUES(NULL, '%d', \"%s\");\n", $country_id, escape_string($parts[6]); 
		}
	}
}

sub escape_string 
{
	my ($string)= @_;
	$string =~ s/'/\\'/g;
	$string =~ s/"/\\"/g;
	return $string;
}
