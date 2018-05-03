# gitHint

## Before
This is free time hobby project, so don't be critical.
I warned you. 

## Abstract
This is a plugin for Idea to analyze VCS statistics and
make small hints about:
1. Who committed max at selected code part.
2. Who made most significant changes at selected code part.
3. Who created file and who deleted it (if it is deleted).
4. Who made only cosmetic changes and who did not.
5. Etc.

The core idea is such info can help user to determine which committer
to ask when code questions arise.

So features to be provided are:
1. Make analysis as mentioned above.
2. Add simple tool window to view statistics.
3. Maybe add chat integration to ask about code as fast as possible.

## Current state
I'm developing the plugin in my free time, so it is slow. Additionally
i'm refreshing my mad code skillz, so some code can be confusing.

## Development
There are some rules to follow if you want to develop:
1. JavaDoc must be everywhere, where code is more complicated
   than array iteration. Every function has its purpose, so it
   must be documented.
2. Every commit must have a small description and small 
   list af changes, including class names if there are significant
   changes in them.
3. If there is a part of code that can be implemented in various
   ways and it can be separated, so it must be separated with
   interfaces. It may change in future, but at start all must be
   as flexible as it can be.
4. If there are TODOs in code, they must be reflected at GitHub.

Current code may not fit these rules, but every new change must.