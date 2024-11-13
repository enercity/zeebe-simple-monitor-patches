# Patch Description

This repository maintains

- a fork from upstream https://github.com/camunda-community-hub/zeebe-simple-monitor
- branches on the tag of the upstream releases,
    - e.g. https://github.com/enercity/zeebe-simple-monitor-patches/tree/release/upstream/v2.7.2
- branches for certain patches / additional features / bugfixes that have not been merged upstream, yet
    - e.g. https://github.com/enercity/zeebe-simple-monitor-patches/tree/feature/importer-metrics
- branches on top of the official branches, that merges the features,
  e.g. https://github.com/enercity/zeebe-simple-monitor-patches/tree/release/v2.7.2-with-patches

This enables this workflow:

1. pull in new releases from upstream
2. create a new branch from the official release with prefix `release/upstream/v...`
3. create a new branch on top of that `release/v...-with-patches`
4. apply the missing features from the `feature`-branches
   a. fix any issues
5. build a release from the `release/v...-with-patches` branch.

This results in:

- a baseline to add new features/bugfixes by simply creating a `feature` branch from the official release branch
- a ready-to-be-build release branch that contains all patches
- a history in that branch that shows the applied patches
- the possibility to rebase the `feature` branches upon new `release/upstream` branches
- a simple "sync from upstream" process, since no conflicts on `main` expected
