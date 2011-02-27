#!/usr/bin/perl
# csv_from_table.pl
use strict;
my $html_file = shift;
my $csv_file  = shift;

my @continents = (
	"ASIA",
	"EUROPE", 
	"AFRICA", 
	"N.AMER",
	"S.AMER", 
	"OCEAN"
);

open (F_CSV, ">", $csv_file)    or  die "Failed to write to file $csv_file : $!";
open (F_HTML, "<", $html_file)  or  die "Failed to read file $html_file : $!";
while (<F_HTML>) {
# read html file line by line
	my $file = $_;
	$file =~ s/<td.*?>/<COMMA>/g;
	$file =~ s/<tr.*?>//g;
	$file =~ s/<\/td>//g;
	$file =~ s/<\/tr>/\n\n/g;
	$file =~ s/<table .*?>//g;
	$file =~ s/<a .*? href/<a href/g;
	$file =~ s/,+/,/g;
	foreach my $con (@continents){
		$file =~ s/$con/\n<\/NEWRECORD>\n<NEWRECORD>\n<CONTINENT>$con<\/CONTINENT>\n/gc;
	}
	$file =~ s/<NEWRECORD>\n,/<NEWRECORD>\n/g;
	$file =~ s/<\/CONTINENT>\n,/<\/CONTINENT>\n/g;
	print F_CSV $file;
}
close (F_HTML);
close (F_CSV);
