> [!WARNING]
> DO NOT MERGE this file to upstream!
>
> It describes our process to patch the original source.

> [!INFORMATION]
> Each custom branch should maintain this file and describe the contents.

<!-- TOC -->

* [How to patch zeebe-simple-monitor](#how-to-patch-zeebe-simple-monitor)
    * [Fork of upstream](#fork-of-upstream)
    * [What branches do exist?](#what-branches-do-exist)
    * [What workflow do we use to patch and release the monitor?](#what-workflow-do-we-use-to-patch-and-release-the-monitor)
    * [Rationales](#rationales)

<!-- TOC -->

# How to patch zeebe-simple-monitor

## Fork of upstream

This repository is a fork from https://github.com/camunda-community-hub/zeebe-simple-monitor ("upstream").

As of 11/2024, upstream seems to be dead. It did not get attention anymore from the maintainers, maybe due to the lack
of time, relevance and Camunda's license change.

Due to that license change (commercial use of Zeebe requires a license), it became highly unlikely that a community will
spend any further efforts on the extensions like Zeebe Exporters or Monitors.

## What branches do exist?

This repository maintains

- a fork of upstream in `main`,
- release branches on the tag of the upstream releases,
    - e.g. https://github.com/enercity/zeebe-simple-monitor-patches/tree/release/upstream/v2.7.2
- branches for certain patches / additional features / bugfixes that have not been merged upstream, yet
    - e.g. https://github.com/enercity/zeebe-simple-monitor-patches/tree/feature/importer-metrics
  - these are based on top of a release branch
  - so upon new releases, they can be rebased or recreated on top of the new release branch
- branches on top of the official release branches, that merges the features,
  e.g. https://github.com/enercity/zeebe-simple-monitor-patches/tree/release/v2.7.2-with-patches

## What workflow do we use to patch and release the monitor?
This enables this workflow:

1. pull in new releases from upstream
2. create a new branch from the official release with prefix `release/upstream/v...`
3. create a new branch on top of that `release/v...-with-patches`
4. apply the missing features from the `feature`-branches
   a. fix any issues
5. build a release from the `release/v...-with-patches` branch.

## Rationales

This process provides us with:

- a baseline to add new features/bugfixes by simply creating a `feature` branch from the official release branch
- a ready-to-be-build release branch that contains all patches
- a history in that branch that shows the applied patches
- the possibility to rebase the `feature` branches upon new `release/upstream` branches
- a simple "sync from upstream" process, since no conflicts on `main` expected
