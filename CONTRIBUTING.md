# Contributing to kp-rpg

You want to contribute to kp-rpg? Welcome! Please read this document to understand what you can do:
 * [Help Others](#help-others)
 * [Analyze Issues](#analyze-issues)
 * [Report an Issue](#report-an-issue)
 * [Contribute Code](#contribute-code)

## Help Others

You can help kp-rpg by helping others who use it and need support.

## Analyze Issues

Analyzing issue reports can be a lot of effort. Any help is welcome!
Go to [the GitHub issue tracker](https://github.com/klenkes74/kp-rpg/issues?state=open) and find an open issue which needs additional work or a bugfix (e.g. issues labeled with "help wanted" or "bug").

Additional work could include any further information, or a gist, or it might be a hint that helps understanding the issue. Maybe you can even find and [contribute](#contribute-code) a bugfix?

## Report an Issue

If you find a bug - behavior of kp-rpg code contradicting your expectation - you are welcome to report it.
We can only handle well-reported, actual bugs, so please follow the guidelines below.

Once you have familiarized with the guidelines, you can go to the [GitHub issue tracker for kp-rpg](https://github.com/klenkes74/kp-rpg/issues/new) to report the issue.

### Quick Checklist for Bug Reports

Issue report checklist:
 * Real, current bug
 * No duplicate
 * Reproducible
 * Good summary
 * Well-documented
 * Minimal example
 * Use the bug issue type and fill the template


### Issue handling process

When an issue is reported, a committer will look at it and either confirm it as a real issue, close it if it is not an issue, or ask for more details.

An issue that is about a real bug is closed as soon as the fix is committed.


### Reporting Security Issues

If you find a security issue, please act responsibly and report it not in the public issue tracker, but directly to us, so we can fix it before it can be exploited.
Please send the related information to [Kaiserpfalz EDV-Service Support](mailto:support@kaiserpfalz-edv.de) using [PGP for e-mail encryption](https://global.sap.com/pc/security/keyblock.txt).
If we don't react within 2 business days, we accept public reporting without complaining about it.

### Usage of Labels

GitHub offers labels to categorize issues. We defined the following labels so far:

Labels for issue categories:
 * bug: this issue is a bug in the code
 * enhancement: this issue is a request for a new functionality or an enhancement request

Status of open issues:
 * help wanted: the feature request is approved and you are invited to contribute

Status/resolution of closed issues:
 * wontfix: while acknowledged to be an issue, a fix cannot or will not be provided

The labels can only be set and modified by committers.


### Issue Reporting Disclaimer

We want to improve the quality of kp-rpg and good bug reports are welcome! But our capacity is limited, thus we reserve the right to close or to not process insufficient bug reports in favor of those which are very cleanly documented and easy to reproduce. Even though we would like to solve each well-documented issue, there is always the chance that it will not happen - remember: kp-rpg is Open Source and comes without warranty.

Bug report analysis support is very welcome! (e.g. pre-analysis or proposing solutions)


## Contribute Code

You are welcome to contribute code to kp-rpg in order to fix bugs or to implement new features.

There are three important things to know:

1.  You must be aware of the LGPL 3.0 (which describes contributions) and **agree to the Contributors License Agreement**. This is common practice in all major Open Source projects.
 For company contributors special rules apply. See the respective section below for details.
2.  There are **several requirements regarding code style, quality, and product standards** which need to be met (we also have to follow them). The respective section below gives more details on the coding guidelines.
3.  **Not all proposed contributions can be accepted**. Some features may e.g. just fit a third-party add-on better. The code must fit the overall direction of kp-rpg and really improve it. The more effort you invest, the better you should clarify in advance whether the contribution fits: the best way would be to just open an issue to discuss the feature you plan to implement (make it clear you intend to contribute).

### Contributor License Agreement

When you contribute (code, documentation, or anything else), you have to be aware that your contribution is covered by the 
same [LGPL 3.0](https://www.gnu.org/licenses/lgpl-3.0.txt) that is applied to kp-rpg itself.
If you want to contribute to parts not licensed on LGPL then you need to agree to the license given for that part of the 
software. Please contact [Support](support@kaiserpfalz-edv.de) for this.
In particular you need to agree to the Individual Contributor License Agreement,
which can be [found here](https://gist.github.com/klenkes74/b76f623edb7830b4344ef2d5e373075e).
(This applies to all contributors, including those contributing on behalf of a company). If you agree to its content, you 
simply have to use the sign-off function of git as during the commit.

#### Company Contributors

If employees of a company contribute code, in **addition** to the individual agreement above, there needs to be one company 
agreement submitted. This is mainly for the protection of the contributing employees.

A company representative authorized to do so needs to download, fill, and print the
[Corporate Contributor License Agreement](https://github.com/klenkes74/kp-rpg/blob/master/KES%20Corporate%20Contributor%20License%20Agreement%20(2019-12-31).pdf)
form. Then either:

- Scan it and e-mail it to [support@kaiserpfalz-edv.de](mailto:support@kaiserpfalz-edv.de)
- Send it by traditional letter to: *Kaiserpfalz EDV-Service, Roland T. Lichti, Darmstädter Str. 12, 64625 Bensheim, 
  GERMANY*


### Contribution Content Guidelines

These are some of the rules we try to follow:

- Apply a clean coding style adapted to the surrounding code, even though we are aware the existing code is not fully 
  clean
- Use (4)spaces for indentation (except if the modified file consistently uses tabs)
- Use variable naming conventions like in the other files you are seeing (camelcase)
- use slf4j for logging
- Comment your code where it gets non-trivial
- Keep an eye on performance and memory consumption, properly destroy objects when not used anymore
- Write a unit test
- Do not do any incompatible changes, especially do not modify the name or behavior of public API methods or properties

### How to contribute - the Process

1. Make sure the change would be welcome (e.g. a bugfix or a useful feature); best do so by proposing it in a GitHub issue
2. Create a branch forking the cla-assistant repository and do your change
3. Commit and push your changes on that branch
4. In the commit message
 - Describe the problem you fix with this change.
 - Describe the effect that this change has from a user's point of view. App crashes and lockups are pretty convincing for 
   example, but not all bugs are that obvious and should be mentioned in the text.
 - Describe the technical details of what you changed. It is important to describe the change in a most understandable way 
   so the reviewer is able to verify that the code is behaving as you intend it to.
5. If your change fixes an issue reported at GitHub, add the following line to the commit message:
  - ```Fixes #(issueNumber)```
  - Do NOT add a colon after "Fixes" - this prevents automatic closing.
6. Create a Pull Request
7. Follow the link posted by the kp-rpg to your pull request and accept it, as described in detail above.
8. Wait for our code review and approval, possibly enhancing your change on request
  - Note that the kp-rpg developers also have their regular duties, so depending on the required effort for reviewing, 
    testing and clarification this may take a while

9. Once the change has been approved we will inform you in a comment
10. We will close the pull request, feel free to delete the now obsolete branch
