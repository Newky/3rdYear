#!/usr/bin/perl
use strict;
use warnings;

my $kb = shift;
my $data = shift;

open (KB, "<", $kb)  or  die "Failed to read file $kb : $!";

my %dict = ();

while (<KB>) {
	my @parts = split /<>/;
	chomp($parts[0]);
	chomp($parts[1]);
	$parts[1] =~ s/"/'/g;
	$dict{$parts[0]} = $parts[1];
}	

open (DATA, "<", $data)  or  die "Failed to read file $data : $!";

while (<DATA>) {
	my $line = $_;
	foreach my $blurb (keys %dict){
		$line =~ s/$blurb/$dict{$blurb}/;
	}
	print $line;
}
